$(document).ready(function() {
    var receiverId;
    var currentUsername;
    var senderUsername; // Variable to store the sender's username



    // Function to populate receiver select dropdown
    function populateReceiverSelect(response) {
        // Populate the receiver select dropdown
        var receiverSelect = $("#addTaskReceiver");
        receiverSelect.empty(); // Clear existing options
        $.each(response, function(index, user) {
            // Exclude the logged-in user's username from options
            if (user.username !== senderUsername) {
                receiverSelect.append($("<option></option>")
                    .attr("value", user.username)
                    .text(user.username));
            }
        });
    }

    // Event listener for the add task button
    $(".add-task-btn").click(function() {
        $.ajax({
            url: "/api/employees/get/allUsername",
            type: "GET",
            dataType: "json",
            success: function(response) {
                populateReceiverSelect(response);
                $("#addTaskSender").val(currentUsername);
            },
            error: function(xhr, status, error) {
                console.error("Error fetching usernames:", error);
            }
        });
    });

    // Event listener for when the add task modal is hidden
    $('#addTaskModal').on('hidden.bs.modal', function (e) {
        // Clear the input fields and reset the select dropdown to its default value
        $('#addTaskForm')[0].reset();
    });

    // Event listener for the add task form submission
    $("#addTaskForm").submit(function(event) {
        // Formun varsayılan davranışını engelle
        event.preventDefault();

        // Form verilerini al
        var formData = {
            name: $("#addTaskName").val(),
            description: $("#addTaskDescription").val(),
            status: $("#addTaskStatus").val(),
            date: $("#addTaskDate").val(),
            sender: currentUsername,
            receiver: $("#addTaskReceiver").val()
        };

        // POST isteği gönder
        $.ajax({
            type: "POST",
            url: "/api/tasks/post/add",
            data: JSON.stringify(formData),
            contentType: "application/json",
            dataType: "json",
            success: function(response) {
                // Başarılı bir şekilde gönderildiğinde yapılacaklar
                console.log("Task başarıyla oluşturuldu:", response);

                // Modalı kapat
                $("#addTaskModal").modal("hide");

                // Görev listesini güncelle
                updateTaskList();
            },
            error: function(xhr, status, error) {
                // Hata durumunda yapılacaklar
                console.error("Task oluşturulurken bir hata oluştu:", error);
                // Kullanıcıya bir hata mesajı gösterebilirsiniz
            }
        });
    });








    // Function to truncate task description
    function truncateDescription(description, maxLength) {
        if (!description) return '';
        if (description.length <= maxLength) return description;
        var truncatedDesc = description.substring(0, maxLength);
        var lastSpaceIndex = truncatedDesc.lastIndexOf(' ');
        if (lastSpaceIndex !== -1) {
            truncatedDesc = truncatedDesc.substring(0, lastSpaceIndex);
        }
        return truncatedDesc + '...';
    }

    // Function to calculate days left until task due date
    function calculateDaysLeft(taskDate) {
        var today = new Date();
        var dueDate = new Date(taskDate);
        var differenceInTime = dueDate.getTime() - today.getTime();
        var differenceInDays = Math.ceil(differenceInTime / (1000 * 3600 * 24));
        if (differenceInDays < 0) {
            return "Late!";
        } else {
            return differenceInDays + " days left";
        }
    }

    // Function to show no data message for a particular status
    function showNoDataMessage(status) {
        $("#" + status.toLowerCase() + "-section").html("<p class='text-danger'>No available data!</p>");
    }

    // Function to update the task list
    function updateTaskList() {
        $.ajax({
            url: "api/tasks/get/receiver/" + receiverId,
            type: "GET",
            dataType: "json",
            success: function(response) {
                var tasksByStatus = {
                    "TODO": [],
                    "IN_PROGRESS": [],
                    "DONE": [],
                    "CANCELLED": []
                };

                $.each(response, function(index, task) {
                    if (task.status.toUpperCase() === "IN PROGRESS") {
                        tasksByStatus["IN_PROGRESS"].push(task);
                    } else {
                        tasksByStatus[task.status].push(task);
                    }
                });

                $.each(tasksByStatus, function(status, tasks) {
                    if (tasks.length === 0) {
                        showNoDataMessage(status);
                    } else {
                        var taskList = $("#" + status.toLowerCase() + "-section");
                        var listHtml = "";
                        $.each(tasks, function(index, task) {
                            var taskHtml = `<li class="task-warning ui-sortable-handle" id="task${task.id}">
                                                <strong>${task.name}</strong>
                                                <div class="clearfix"></div>
                                                <div class="mt-3">
                                                    <p class="description">${truncateDescription(task.description, 127)}</p>
                                                    <p class="float-right mb-0 mt-2">
                                                        <button type="button" class="btn btn-success btn-sm waves-effect waves-light view-task" data-taskid="${task.id}">View</button>
                                                    </p>
                                                    <p class="mb-0">
                                                        <a href="/profile/${task.senderUsername}" class="text-muted">
                                                            <img src="https://bootdey.com/img/Content/avatar/avatar1.png" alt="task-sender" class="thumb-sm rounded-circle mr-2">
                                                            <span id="${task.senderUsername}" class="font-bold font-secondary">${task.senderUsername}</span>
                                                        </a>
                                                    </p>
                                                </div>
                                            </li>`;
                            listHtml += taskHtml;
                        });
                        taskList.html(listHtml);
                    }
                });
            },
            error: function(xhr, status, error) {
                console.error("Error fetching tasks:", error);
            }
        });
    }

    // Get the receiver ID on document ready
    $.ajax({
        url: "/auth/get/user",
        type: "GET",
        dataType: "json",
        success: function(response) {
            receiverId = response.id;
            currentUsername = response.username;
            updateTaskList();
        },
        error: function(xhr, status, error) {
            console.error("Error fetching user data:", error);
        }
    });

    // View task details when clicking on View button
    $(document).on("click", ".view-task", function() {
        var taskId = $(this).data("taskid");
        $.ajax({
            url: "/api/tasks/get/" + taskId,
            type: "GET",
            dataType: "json",
            success: function(response) {
                $("#taskDetailModal").load("/static/employee/dashboard/taskDetailModal.html", function() {
                    $('.modal-backdrop').remove();
                    // Set the taskid data attribute
                    $("#taskDetailsModal").data("taskid", response.id);
                    $("#taskDetailLabel").text("#"+ response.id + " Task Details");
                    $("#taskName").text(response.name);
                    $("#taskDescription").text(response.description);
                    $("#taskDate").text(response.date.replaceAll("-","/"));
                    // Seçici değiştiğinde
                    $('#taskStatus').change(function() {
                        // Seçili option'un class'ını al
                        var selectedClass = $('option:selected', this).attr('class');
                        // Seçiciye seçili option'un class'ını ekle
                        $(this).removeClass().addClass('form-select w-auto badge rounded-pill text-light ' + selectedClass);
                    });

                    $("#taskStatus").val(response.status).trigger('change');
                    $("#taskSender").text(response.senderUsername);
                    $("#senderProfile").attr("href", "/profile/"+response.senderUsername);


                    if (response.date) {
                        var daysLeft = calculateDaysLeft(response.date);
                        if (daysLeft.includes("Late!")) {
                            $("#daysLeft").text(daysLeft).addClass("text-danger").show();
                        } else {
                            $("#daysLeft").text(daysLeft).addClass("text-muted").show();
                        }
                    } else {
                        $("#daysLeft").hide();
                    }
                    $("#taskDetailsModal").modal("show");
                });
            },
            error: function(xhr, status, error) {
                console.error("Error fetching task details:", error);
            }
        });
    });

    // Remove modal-open class when task details modal is hidden
    $('#taskDetailsModal').on('hidden.bs.modal', function () {
        $('body').removeClass('modal-open');
    });

    // Show update status button when task status is changed
    $(document).on("change", "#taskStatus", function() {
        $(".update-status-btn").show();
    });

    // Update task status when update status button is clicked
    $(document).on("click", ".update-status-btn", function() {
        var newStatus = $("#taskStatus").val();
        var taskId = $("#taskDetailsModal").data("taskid");

        $.ajax({
            url: "/api/tasks/put/update/status",
            type: "PUT",
            dataType: "json",
            contentType: "application/json",
            data: JSON.stringify({ id: taskId, status: newStatus }),
            success: function(response) {
                updateTaskList(); // Görev listesini güncelle
                // Modalı kapat
                $('#taskDetailsModal').modal('hide');
            },
            error: function(xhr, status, error) {
                console.error("Error updating task status:", error);
            }
        });
    });

    // Date picker settings
            $(document).ready(function () {
                // Tarih alanını Bootstrap Datepicker ile etkinleştirme
                $('.datepicker').datepicker({
                    format: 'yyyy-mm-dd', // Tarih formatı
                    autoclose: true, // Takvim seçildiğinde otomatik olarak kapatma
                    language: 'tr', // Türkçe dil desteği
                    todayHighlight: true, // Bugünü vurgula
                    startDate: '0'

                });

            });

});
