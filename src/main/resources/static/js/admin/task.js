$(document).ready(function () {
        // Verileri API'den çekme
        $.ajax({
            url: '/api/tasks/get/all',
            method: 'GET',
            success: function (data) {
                // Tabloya verileri ekleme
                data.forEach(task => {
                    let description = task.description;
                        let descriptionRows = [];

                        while (description.length > 0) {
                            let row = description.substring(0, 64);
                            let lastSpaceIndex = row.lastIndexOf(" ");

                            if (lastSpaceIndex !== -1 && description.length > 64) {
                                descriptionRows.push(row.substring(0, lastSpaceIndex));
                                description = description.substring(lastSpaceIndex + 1);
                            } else {
                                descriptionRows.push(row);
                                description = description.substring(row.length);
                            }
                        }

                        const formattedDescription = descriptionRows.join("<br>");
                    $('#tasksTable tbody').append(`
                        <tr>
                            <td>${task.id}</td>
                            <td>${task.name}</td>
                            <td>${formattedDescription}</td>
                            <td>${task.status}</td>
                            <td>${task.date}</td>
                            <td>${task.senderUsername}</td>
                            <td>${task.receiverUsername}</td>
                            <td>
                                <button id="editButton_${task.id}" class="btn btn-primary editTaskBtn"><i class="fas fa-edit"></i>EDIT</button>
                                <button id="deleteButton_${task.id}" class="btn btn-danger deleteTaskBtn"><i class="fas fa-trash-alt"></i>DELETE</button>
                            </td>
                        </tr>
                    `);
                });
                // DataTables eklentisini başlatma
                $('#tasksTable').DataTable();
            },
            error: function (error) {
                console.error('Error fetching data:', error);
            }
        });

        // Edit button click event
        $(document).on('click', '.editTaskBtn', function () {
            // Get the task ID from the button's ID attribute
            var buttonId = $(this).attr('id');
            var taskId = parseInt(buttonId.split('_')[1]); // Butonun ID'sinden görev kimliğini al ve integer'a dönüştür

            // AJAX request to fetch task details by ID
            $.ajax({
                url: '/api/tasks/get/' + taskId,
                method: 'GET',
                success: function (task) {
                    // Edit modalını doldur
                    $('#editName').val(task.name);
                    $('#editDescription').val(task.description);
                    $('#editStatus').val(task.status);
                    $('#editDate').val(task.date);

                    // AJAX request to fetch all employees for sender and receiver options
                    $.ajax({
                        url: '/api/employees/get/all',
                        method: 'GET',
                        success: function (employees) {
                            // Gönderen (Sender) seçeneklerini doldur
                            var senderSelect = $('#editSender');
                            senderSelect.empty(); // Önceki seçenekleri temizle
                            employees.forEach(function (employee) {
                                senderSelect.append('<option value="' + employee.username + '">' + employee.username + '</option>');
                            });
                            // Gönderenini görevin gönderenine ayarla
                            senderSelect.val(task.senderUsername);

                            // Alıcı (Receiver) seçeneklerini doldur
                            var receiverSelect = $('#editReceiver');
                            receiverSelect.empty(); // Önceki seçenekleri temizle
                            employees.forEach(function (employee) {
                                receiverSelect.append('<option value="' + employee.username + '">' + employee.username + '</option>');
                            });
                            // Alıcısını görevin alıcısına ayarla
                            receiverSelect.val(task.receiverUsername);

                            // Edit modalını göster
                            $('#editTaskModal').modal('show');
                        },
                        error: function (error) {
                            console.error('Error fetching employees for sender and receiver:', error);
                        }
                    });

                    // Task ID'sini global olarak tanımla
                    window.taskId = taskId;
                },
                error: function (error) {
                    console.error('Error fetching task data for editing:', error);
                }
            });
        });


        // Görev güncelleme formu submit işlemi
        $('#editTaskForm').submit(function (event) {
            event.preventDefault(); // Formun normal submit işlemini engelle

            // Formdaki verileri al
            var formData = {
                id: window.taskId,
                name: $('#editName').val(),
                description: $('#editDescription').val(),
                status: $('#editStatus').val(),
                date: $('#editDate').val(),
                sender: $('#editSender').val(),
                receiver: $('#editReceiver').val()
            };

            // Görev güncelleme isteği
            $.ajax({
                url: '/api/tasks/put/update',
                method: 'PUT',
                contentType: 'application/json',
                data: JSON.stringify(formData),
                success: function (response) {
                    // Başarılı bir şekilde güncellendikten sonra modalı gizle
                    $('#editTaskModal').modal('hide');

                    // Güncellenen veriyi tablodan güncelleme işlemi
                    var table = $('#tasksTable').DataTable();

                    // Tablodan eski satırı kaldır
                    table.row($('#editTaskModal').data('row')).remove().draw(false);

                    // Yeni veriyi tabloya ekle
                    table.row.add([
                        response.id,
                        response.name,
                        response.description,
                        response.status,
                        response.date,
                        response.senderUsername,
                        response.receiverUsername,
                        '<button id="editButton_' + response.id + '" class="btn btn-primary editTaskBtn"><i class="fas fa-edit"></i>EDIT</button> ' +
                        '<button id="deleteButton_' + response.id + '" class="btn btn-danger deleteTaskBtn"><i class="fas fa-trash-alt"></i>DELETE</button>'
                    ]).draw(false);
                },
                error: function (error) {
                    console.error('Error updating task:', error);
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


        // Add Task button click event
            $('.add-task-btn').click(function () {
                // Temizleme işlemi: Modal içindeki input değerlerini temizle
                $('#addTaskName').val('');
                $('#addTaskDescription').val('');
                $('#addTaskStatus').val('TODO');
                $('#addTaskDate').val('');
                $('#addTaskSender').val('');
                $('#addTaskReceiver').val('');

                // AJAX request to fetch all employees for sender and receiver options
                $.ajax({
                    url: '/api/employees/get/all',
                    method: 'GET',
                    success: function (employees) {
                        // Gönderen (Sender) seçeneklerini doldur
                        var senderSelect = $('#addTaskSender');
                        senderSelect.empty(); // Önceki seçenekleri temizle
                        employees.forEach(function (employee) {
                            senderSelect.append('<option value="' + employee.username + '">' + employee.username + '</option>');
                        });

                        // Alıcı (Receiver) seçeneklerini doldur
                        var receiverSelect = $('#addTaskReceiver');
                        receiverSelect.empty(); // Önceki seçenekleri temizle
                        employees.forEach(function (employee) {
                            receiverSelect.append('<option value="' + employee.username + '">' + employee.username + '</option>');
                        });

                        // Add Task modalını göster
                        $('#addTaskModal').modal('show');
                    },
                    error: function (error) {
                        console.error('Error fetching employees for sender and receiver:', error);
                    }
                });
            });

            // Add Task form submit event
            $('#addTaskForm').submit(function (event) {
                event.preventDefault(); // Formun normal submit işlemini engelle

                // Formdaki verileri al
                var formData = {
                    name: $('#addTaskName').val(),
                    description: $('#addTaskDescription').val(),
                    status: $('#addTaskStatus').val(),
                    date: $('#addTaskDate').val(),
                    sender: $('#addTaskSender').val(),
                    receiver: $('#addTaskReceiver').val()
                };

                // Görev oluşturma isteği
                $.ajax({
                    url: '/api/tasks/post/add',
                    method: 'POST',
                    contentType: 'application/json',
                    data: JSON.stringify(formData),
                    success: function (response) {
                        // Başarılı bir şekilde ekledikten sonra modalı gizle
                        $('#addTaskModal').modal('hide');

                        // Yeni veriyi tabloya ekleme işlemi
                        var table = $('#tasksTable').DataTable();
                        table.row.add([
                            response.id,
                            response.name,
                            response.description,
                            response.status,
                            response.date,
                            response.senderUsername,
                            response.receiverUsername,
                            '<button id="editButton_' + response.id + '" class="btn btn-primary editTaskBtn"><i class="fas fa-edit"></i>EDIT</button> ' +
                            '<button id="deleteButton_' + response.id + '" class="btn btn-danger deleteTaskBtn"><i class="fas fa-trash-alt"></i>DELETE</button>'
                        ]).draw(false);
                    },
                    error: function (error) {
                        console.error('Error adding task:', error);
                    }
                });
            });


           // Delete button click event
        $('#tasksTable tbody').on('click', '.deleteTaskBtn', function () {
               var buttonId = $(this).attr('id');
               var taskId = buttonId.split('_')[1]; // Butonun ID'sinden görev kimliğini al

               $('#deleteConfirmationModal').modal('show'); // Modalı göster

               var $row = $(this).closest('tr'); // Silinecek satırı seç

               // Sil butonuna tıklanırsa
               $('#confirmDeleteBtn').on('click', function () {
                   // Kullanıcıya silmek isteyip istemediğine dair bir onay kutusu göster
                   $.ajax({
                       url: '/api/tasks/delete/' + taskId,
                       method: 'DELETE',
                       success: function () {
                           // Başarılı bir silme işleminden sonra tabloyu yeniden yükleme
                           var table = $('#tasksTable').DataTable();
                           table.row($row).remove().draw(); // Satırı sil ve tabloyu güncelle
                           $('#deleteConfirmationModal').modal('hide'); // Modalı gizle
                       },
                       error: function (error) {
                           console.error('Error deleting task:', error);
                       }
                });
            });
        });



});