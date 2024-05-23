$(document).ready(function () {
    // Verileri API'den çekme ve tabloya ekleme işlemi
    $.ajax({
        url: '/api/employees/get/all',
        method: 'GET',
        success: function (data) {
            data.forEach(employee => {
                var role = employee.role.replace('ROLE_', '');
                role = role.charAt(0).toUpperCase() + role.slice(1).toLowerCase();
                $('#employeeTable tbody').append(`
                    <tr data-username="${employee.username}">
                        <td>${employee.id}</td>
                        <td>${employee.username}</td>
                        <td>${employee.name}</td>
                        <td>${employee.lastname}</td>
                        <td>${employee.mail}</td>
                        <td>${role}</td>
                        <td>
                            <button id="editButton_${employee.id}" class="btn btn-primary editEmployeeBtn">EDIT</button>
                            <button id="deleteButton_${employee.id}" class="btn btn-danger deleteEmployeeBtn">DELETE</button>
                        </td>
                    </tr>
                `);
            });
            $('#employeeTable').DataTable();
        },
        error: function (error) {
            console.error('Error fetching data:', error);
        }
    });

    // Tablodaki her satıra tıklama olayını ekleme
    $('#employeeTable tbody').on('click', 'tr', function (e) {
        // Tıklanan satırdaki kullanıcı adını al
    var username = $(this).find('td:eq(1)').text(); // Username sütununun (1. sütun) içeriğini al
        // Eğer tıklanan yer buton ise veya butonun içerisinde bir elementse, yönlendirme işlemi yapma
        if ($(e.target).closest('.editEmployeeBtn, .deleteEmployeeBtn').length === 0) {
            // Kullanıcı adına göre detay sayfasına yönlendirme işlemi
            window.location.href = '/admin/employees/details/' + username;
        }
    });

    // Yeni çalışan ekleme formu submit işlemi
    $('#addEmployeeForm').submit(function (event) {
        event.preventDefault(); // Formun normal submit işlemini engelle
        // Formdaki verileri al
        var formData = {
            username: $('#username').val(),
            name: $('#name').val(),
            lastname: $('#lastname').val(),
            mail: $('#mail').val(),
            password: $('#password').val(),
            role: $('#role').val(),
            gender: $('#gender').val(),
            address: $('#address').val(),
            phone: $('#phone').val(),
            mobile: $('#mobile').val(),
            position: $('#position').val()
        };
        // Yeni çalışanı API'ye post etme
        $.ajax({
            url: '/api/employees/post/add',
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(formData),
            success: function (response) {
            var role = response.employee.role.replace('ROLE_', '');
                            role = role.charAt(0).toUpperCase() + role.slice(1).toLowerCase();
                // Yeni çalışan başarıyla eklendikten sonra tabloyu güncelleme
                $('#employeeTable').DataTable().row.add([
                    response.employee.id,
                    response.employee.username,
                    response.name,
                    response.lastname,
                    response.employee.mail,
                    role,
                    '<button id="editButton_' + response.employee.id + '" class="btn btn-primary editEmployeeBtn"><i class="fas fa-edit"></i>EDIT</button> ' +
                    '<button id="deleteButton_' + response.employee.id + '" class="btn btn-danger deleteEmployeeBtn"><i class="fas fa-trash-alt"></i>DELETE</button>'
                ]).draw(false);
                // Formu temizleme
                $('#addEmployeeForm')[0].reset();
                // Popup'ı kapatma
                $('#addEmployeeModal').modal('hide');
            },
            error: function (error) {
                console.error('Error adding new employee:', error);
            }
        });
    });

     // Delete düğmesine tıklama işleyicisi
     $('#employeeTable tbody').on('click', '.deleteEmployeeBtn', function () {
         var buttonId = $(this).attr('id');
         var employeeId = buttonId.split('_')[1]; // Butonun ID'sinden kullanıcı kimliğini al
         $('#deleteConfirmationModal').modal('show'); // Modalı göster
         var $row = $(this).closest('tr'); // Silinecek satırı seç
         // Sil butonuna tıklanırsa
         $('#confirmDeleteBtn').on('click', function() {
             // Kullanıcıya silmek isteyip istemediğine dair bir onay kutusu göster
             $.ajax({
                 url: '/api/employees/delete/' + employeeId,
                 method: 'DELETE',
                 success: function () {
                     // Başarılı bir silme işleminden sonra tabloyu yeniden yükleme
                     $('#employeeTable').DataTable().row($row).remove().draw(); // Satırı sil ve tabloyu güncelle
                     $('#deleteConfirmationModal').modal('hide'); // Modalı gizle
                 },
                 error: function (error) {
                     console.error('Error deleting employee:', error);
                 }
             });
         });
     });


     // Edit düğmesine tıklama işleyicisi
     $('#employeeTable tbody').on('click', '.editEmployeeBtn', function () {
         var buttonId = $(this).attr('id');
         var employeeId = parseInt(buttonId.split('_')[1]); // Butonun ID'sinden kullanıcı kimliğini al ve integer'a dönüştür
         var $existingRow = $(this).closest('tr');

         $.ajax({
             url: '/api/employees/getDetails/' + employeeId,
             method: 'GET',
             success: function (employee) {
                 // Edit modalını doldur
                 $('#editUsername').val(employee.username);
                 $('#editName').val(employee.name);
                 $('#editLastname').val(employee.lastname);
                 $('#editMail').val(employee.mail);
                 // Cinsiyetin ilk harfini büyük yaparak cinsiyet seçeneğini belirle
                 var capitalizedGender = employee.gender.charAt(0).toUpperCase() + employee.gender.slice(1);
                 $('#editGender').val(capitalizedGender).change();
                 var roleWithoutPrefix = employee.role.replace('ROLE_', '');
                 var capitalizedRole = roleWithoutPrefix.toLowerCase();
                 roleWithoutPrefix = capitalizedRole.charAt(0).toUpperCase() + capitalizedRole.slice(1);
                 if(roleWithoutPrefix.toUpperCase() ==="ADMIN"){
                 $('#roleDiv').hide();}else{
                 $('#roleDiv').show();
                 $('#editRole').val(roleWithoutPrefix).change();
                 }
                 $('#editAddress').val(employee.address);
                 $('#editPhone').val(employee.phone);
                 $('#editMobile').val(employee.mobile);
                 $('#editPosition').val(employee.position);

                 // Edit modalını göster
                 $('#editEmployeeModal').modal('show');

                 //employee Id global tanımlama
                 window.employeeId = employeeId;
                 window.existingRow = $existingRow;
             },
             error: function (error) {
                 console.error('Error fetching employee data for editing:', error);
             }
         });
     });


    // Çalışanı güncelleme formu submit işlemi
    $('#editEmployeeForm').submit(function (event) {
        event.preventDefault(); // Formun normal submit işlemini engelle

        // Formdaki verileri al
        var formData = {
            id: window.employeeId,
            username: $('#editUsername').val(),
            password: $('#editPassword').val(),
            name: $('#editName').val(),
            lastname: $('#editLastname').val(),
            mail: $('#editMail').val(),
            role: 'ROLE_' + $('#editRole').val().toUpperCase(), // Seçilen role'ı düzelt ve büyük harf yap
            gender: $('#editGender').val(),
            address: $('#editAddress').val(),
            phone: $('#editPhone').val(),
            mobile: $('#editMobile').val(),
            position: $('#editPosition').val()
        };

        // Çalışanı güncelleme isteği
        $.ajax({
            url: '/api/employees/put/update',
            method: 'PUT',
            contentType: 'application/json',
            data: JSON.stringify(formData),
            success: function (response) {
            var role = response.employee.role.replace('ROLE_', '');
                            role = role.charAt(0).toUpperCase() + role.slice(1).toLowerCase();
                // Başarılı bir şekilde güncellendikten sonra modalı gizle
                $('#editEmployeeModal').modal('hide');

                // Güncellenen veriyi tablodan güncelle
                var table = $('#employeeTable').DataTable();

                // Eski satırı tablodan kaldır
                table.row(window.existingRow).remove().draw(false);

                // Yeni satırı tabloya ekle
                table.row.add([
                    response.employee.id,
                    response.employee.username,
                    response.name,
                    response.lastname,
                    response.employee.mail,
                    role,
                    '<button id="editButton_' + response.employee.id + '" class="btn btn-primary editEmployeeBtn"><i class="fas fa-edit"></i>EDIT</button> ' +
                    '<button id="deleteButton_' + response.employee.id + '" class="btn btn-danger deleteEmployeeBtn"><i class="fas fa-trash-alt"></i>DELETE</button>'
                ]).draw(false);
            },
            error: function (error) {
                console.error('Error updating employee:', error);
            }
        });
    });









});
