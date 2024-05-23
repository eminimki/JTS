$(document).ready(function () {
    // Verileri API'den çekme
    $.ajax({
        url: '/api/logs/get/all',
        method: 'GET',
        success: function (data) {
            // Tabloya verileri ekleme
            data.forEach(log => {
                $('#logTable tbody').append(`
                    <tr>
                        <td>${log.id}</td>
                        <td>${log.actioner}</td>
                        <td>${log.description}</td>
                        <td>${log.time}</td>
                        <td>
                            <button class="btn btn-danger deleteLogBtn" data-toggle="modal" data-target="#deleteConfirmationModal" data-logid="${log.id}">Delete</button>
                        </td>
                    </tr>
                `);
            });
            // DataTables eklentisini başlatma
            $('#logTable').DataTable();
        },
        error: function (error) {
            console.error('Error fetching data:', error);
        }
    });

       // Delete button click event
            $('#logTable tbody').on('click', '.deleteLogBtn', function () {
                    var logId = $(this).data('logid');
                   $('#deleteLogConfirmationModal').modal('show'); // Modalı göster

                   var $row = $(this).closest('tr'); // Silinecek satırı seç

                   // Sil butonuna tıklanırsa
                   $('#confirmDeleteBtn').on('click', function () {

                       $.ajax({
                           url: '/api/logs/delete/' + logId,
                           method: 'DELETE',
                           success: function () {
                               // Başarılı bir silme işleminden sonra tabloyu yeniden yükleme
                               var table = $('#logTable').DataTable();
                               table.row($row).remove().draw(); // Satırı sil ve tabloyu güncelle
                               $('#deleteLogConfirmationModal').modal('hide'); // Modalı gizle
                           },
                           error: function (error) {
                               console.error('Error deleting log:', error);
                           }
                    });
                });
            });
});
