$(document).ready(function () {
    var username;

    // Kullanıcı bilgilerini almak için AJAX isteği gönder
    $.ajax({
        url: "/auth/get/user",
        type: "GET",
        dataType: "json",
        success: function(response) {
            // Kullanıcı verisini alın
            username = response.username;

            // Log verilerini çekme
            $.ajax({
                url: '/api/logs/get/username/'+ username,
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
        },
        error: function(xhr, status, error) {
            console.error("Error fetching user data:", error);
        }
    });
});
