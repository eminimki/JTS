$(document).ready(function() {
// URL'den alınan kullanıcı adını saklayacak değişken
    var username;
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

    // URL'yi kontrol et ve /profile/{username} adresindeki kullanıcı adını al
    var url = window.location.href;
    var profileIndex = url.indexOf('/profile/');
    if (profileIndex !== -1) {
        // /profile/ bulunursa, bu kısmı al ve sonrasını kullanıcı adı olarak kabul et
        username = url.substring(profileIndex + 9); // 9, '/profile/' uzunluğu
    } else {
        // /profile/ bulunamazsa, mevcut kullanıcıyı al
        $.ajax({
            url: "/auth/get/user",
            type: "GET",
            dataType: "json",
            success: function(response) {
                // Mevcut kullanıcının kullanıcı adını sakla
                username = response.username;
                // URL'yi güncelle ve mevcut kullanıcının profil sayfasına yönlendir
                window.location.href = "/profile/" + username;
            },
            error: function(xhr, status, error) {
                console.error("Error fetching user data:", error);
                // Hata durumunda bir işlem yapabilirsiniz
            }
        });
    }


    // API'den veri almak için AJAX isteği gönder
    $.ajax({
        url: '/api/employees/get/username/' + username,
        type: 'GET',
        success: function(response) {
            // API'den gelen verileri kullanarak sayfayı doldur
            $('#username').text(response.username);
            $('#position').text(response.position);
            var roleWithoutPrefix = response.role.replace('ROLE_', '');
            var formattedRole = roleWithoutPrefix.charAt(0).toUpperCase() + roleWithoutPrefix.slice(1).toLowerCase();
            $('#role').text(formattedRole);
            $('#name').text(response.name);
            $('#lastname').text(response.lastname);
            $('#email').text(response.mail);
            $('#phone').text(response.phone);
            $('#mobile').text(response.mobile);
            $('#address').text(response.address);
            var formattedGender = response.gender.charAt(0).toUpperCase() + response.gender.slice(1).toLowerCase();
            $('#gender').text(formattedGender);
            setGenderAvatar(response.gender);
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
            if(total !== 0){
                $("#noReceiverDataWarning").hide();
            }

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
            if(total !== 0){
                $("#noSenderDataWarning").hide();
            }

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