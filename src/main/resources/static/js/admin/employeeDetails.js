$(document).ready(function() {
    // URL'den username parametresini al
    var url = window.location.href;
    var username = url.substring(url.lastIndexOf('/') + 1);

    function setGenderAvatar(gender){
    var avatarUrl = "";
                if (gender.toUpperCase() === "MALE") {
                    avatarUrl = "https://mdbcdn.b-cdn.net/img/Photos/new-templates/bootstrap-chat/ava3.webp";
                } else if (gender.toUpperCase() === "FEMALE") {
                    avatarUrl = "https://mdbcdn.b-cdn.net/img/Photos/new-templates/bootstrap-chat/ava2.webp";
                } else {
                    avatarUrl = "https://mdbcdn.b-cdn.net/img/Photos/new-templates/bootstrap-chat/ava1.webp";
                }
                $("#avatar").attr("src", avatarUrl);
    }


    // API'den veri almak için AJAX isteği gönder
    $.ajax({
        url: '/api/employees/get/username/' + username,
        type: 'GET',
        success: function(response) {
            // API'den gelen verileri kullanarak sayfayı doldur
            $('#edit-username').val(response.username);
            $('#edit-position').val(response.position);
            var roleWithoutPrefix = response.role.replace('ROLE_', '');
            if(roleWithoutPrefix === "ADMIN"){
            $('#edit-role').hide();
            $("#role").append(`<p class="mb-0 borderless-input-first-card">Admin</p>`);
            }else{$('#edit-role').val(roleWithoutPrefix).change();}
            $('#name').val(response.name);
            $('#lastname').val(response.lastname);
            $('#email').val(response.mail);
            $('#phone').val(response.phone);
            $('#mobile').val(response.mobile);
            $('#address').val(response.address);
            $('#gender').val(response.gender);
            $("#deleteButton").attr("data-employee-id", response.id);
            $("#editButton").attr("data-employee-id", response.id);
            setGenderAvatar(response.gender);
            employeeId = response.id;



        },
        error: function(xhr, status, error) {
                if (xhr.status == 400) {
                    // 400 hatası durumunda sayfayı gizle veya istenilen işlemi yap
                    $('body').html('<div class="container py-5"><h1 class="text-center">Error</h1><p class="text-center">Employee not found!</p></div>');
                } else {
                    // Diğer hata durumlarında genel bir hata mesajı göster
                    console.error(xhr.responseText);
                    alert('An error occurred while fetching employee data.');
                }
            }
    });


    // Delete button click event handler
        $("#deleteButton").click(function(){
            var employeeId = $(this).data("employee-id");
            $("#deleteConfirmButton").attr("data-employee-id", employeeId);
            // Show confirmation modal
            $('#confirmationModalTextArea').text("Are you sure you want to delete this employee?");
            $("#editConfirmButton").hide()
            $("#deleteConfirmButton").show()
            $("#confirmationModal").modal("show");
        });

        // Confirm delete button click event handler
        $("#deleteConfirmButton").click(function(){
            // Assuming employeeId is stored in a data attribute of the delete button
            var employeeId = $(this).data("employee-id");

            // Send DELETE request to the server
            $.ajax({
                url: "/api/employees/delete/" + employeeId,
                type: "DELETE",
                success: function(response){
                    $('#successModelTextArea').text("Deletion successful!");
                    // Show success modal
                    $("#successModal").modal("show");
                    // Redirect to /admin/employees after clicking Close on the success modal
                    $("#successModal").on("hidden.bs.modal", function () {
                        window.location.href = "/admin/employees";
                    });
                },
                error: function(xhr, status, error){
                    // Show error message in a popup
                    alert("An error occurred while deleting the employee.");
                    console.error(xhr.responseText);
                }
            });

            // Close the confirmation modal
            $("#confirmationModal").modal("hide");
        });


         // Edit button click event handler
            $("#editButton").click(function() {
                // Show confirmation modal
                var employeeId = $(this).data("employee-id");
                $("#editConfirmButton").attr("data-employee-id", employeeId);
                // Show confirmation modal
                $('#confirmationModalTextArea').text("Are you sure you want to edit this employee?");
                $("#editConfirmButton").show()
                $("#deleteConfirmButton").hide()
                $("#confirmationModal").modal("show");

            });

            // Confirm edit button click event handler
            $("#editConfirmButton").click(function() {

                // Alınan verileri bir nesne içerisinde topla
                var employeeId = $("#editButton").data("employee-id");
                var employeeData = {
                    id: employeeId,
                    username: $('#edit-username').val(),
                    position: $('#edit-position').val(),
                    role: 'ROLE_' + $('#edit-role').val().toUpperCase(),
                    name: $('#name').val(),
                    lastname: $('#lastname').val(),
                    mail: $('#email').val(),
                    phone: $('#phone').val(),
                    mobile: $('#mobile').val(),
                    address: $('#address').val(),
                    gender: $('#gender').val()
                };

                // AJAX PUT isteği gönder
                $.ajax({
                    url: "/api/employees/put/updateNoPassword",
                    type: "PUT",
                    contentType: "application/json",
                    data: JSON.stringify(employeeData),
                    success: function(response) {
                        $("#confirmationModal").modal("hide");
                        // Success modal içerisine yazıyı güncelle
                        $('#successModelTextArea').text("Update successful!");
                        // Success modalı göster
                        $("#successModal").modal("show");
                        setGenderAvatar(employeeData.gender);
                    },
                    error: function(xhr, status, error) {
                        // Hata durumunda genel bir hata mesajı göster
                        alert("An error occurred while updating the employee.");
                        console.error(xhr.responseText);
                    }
                });

                // Close the edit confirmation modal
                $("#editConfirmationModal").modal("hide");
            });


            // Receiver Task Statistics Response
            $.ajax({
                url: "/api/tasks/get/statistics/receiver/" + username,
                type: "GET",
                success: function(response) {
                    var total = 0;

                    // Her bir durum için count'ları topla
                    response.forEach(function(item) {
                        // Her bir durumun count'ını toplam değişkenine ekle
                        total += item.count;
                    });
                    if(total !== 0){$("#noReceiverDataWarning").hide();}


                    // Her bir durum için ilerleme çubuğu oluştur
                    response.forEach(function(item) {
                        // Durum adını al
                        var status = item.status;
                        var width = (item.count / total) * 100;
                        width = width | 0;


                        // Durum adını kullanarak bir ilerleme çubuğu oluştur
                        var progressHTML = `
                            <div class="row">
                                <div class="col-7 ">${status}</div>
                                <div class="col-5 text-right">${width}%</div>
                            </div>
                            <div class="progress rounded mb-2" style="height: 5px;">
                                <div class="progress-bar" role="progressbar" style="width: ${width}%" aria-valuenow="${width}"
                                    aria-valuemin="0" aria-valuemax="100"></div>
                            </div>
                        `;

                        // İlerleme çubuğunu sayfaya ekle
                        $("#receiverProgressBars").append(progressHTML);
                    });
                },
                error: function(xhr, status, error) {
                    // Hata durumunda yapılacak işlemler
                    console.error("Hata:", error);
                    alert("Veri alınırken bir hata oluştu.");
                }
            });

            // Sender Task Statistics Response
            $.ajax({
            url: "/api/tasks/get/statistics/sender/" + username,
            type: "GET",
            success: function(response) {
                var total = 0;

                // Her bir durum için count'ları topla
                response.forEach(function(item) {
                    // Her bir durumun count'ını toplam değişkenine ekle
                    total += item.count;
                });
                if(total !== 0){$("#noSenderDataWarning").hide();}

                // Her bir durum için ilerleme çubuğu oluştur
                response.forEach(function(item) {
                    // Durum adını al
                    var status = item.status;
                    var width = (item.count / total) * 100;
                    width = width | 0;
                    // Durum adını kullanarak bir ilerleme çubuğu oluştur
                    var progressHTML = `
                    <div class="row">
                        <div class="col-7 ">${status}</div>
                        <div class="col-5 text-right">${width}%</div>
                    </div>
                    <div class="progress rounded mb-2" style="height: 5px;">
                    <div class="progress-bar" role="progressbar" style="width: ${width}%" aria-valuenow="${width}"
                    aria-valuemin="0" aria-valuemax="100"></div>
                    </div>
                    `;

                    // İlerleme çubuğunu sayfaya ekle
                    $("#senderProgressBars").append(progressHTML);
                });
            },
            error: function(xhr, status, error) {
            console.error("Hata:", error);
            alert("Veri alınırken bir hata oluştu.");
            }
         });





});
