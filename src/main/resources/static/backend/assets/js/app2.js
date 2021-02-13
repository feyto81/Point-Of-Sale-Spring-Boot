"use strict";
$(function () {
    $('.sumo_search').SumoSelect({search: true, searchText: 'Search....'});
    $('.sumo').SumoSelect();
    $(".flatPicker").flatpickr();
})

$("table #checkall").click(function () {
    var is_checked = $(this).is(":checked");
    $("table .checkbox").prop("checked", !is_checked).trigger("click");
});


function hapusData(url){
    let returnUrl = window.location.href;
    swal({
        title: 'Delete !',
        text: 'Apakah anda yakin akan menghapus data ini?',
        icon: 'warning',
        buttons: true,
        dangerMode: true,
    })
    .then((willDelete) => {
        if (willDelete) {
            location.href=url+"?return_url="+encodeURIComponent(returnUrl);
        } else {
            swal('Your imaginary file is safe!');
        }
    });
}


$(function () {
    $('.preload').fadeOut(300);
    $(".fadeModal").modal({
        fadeDuration: 100
    });
    $('.image-popup').magnificPopup({
        type: 'image',
        closeOnContentClick: true,
        closeBtnInside: false,
        fixedContentPos: true,
        mainClass: 'mfp-no-margins mfp-with-zoom', // class to remove default margin from left and right side
        image: {
            verticalFit: true
        },
        zoom: {
            enabled: true,
            duration: 300 // don't foget to change the duration also in CSS
        }
    });
});
function deleteSelected() {
    swal({
        title: 'Konfirmasi',
        text: 'Apakah anda yakin untuk Hapus terpilih ?',
        icon: 'warning',
        buttons: true,
        dangerMode: true,
    })
        .then((willDelete) => {
            if (willDelete) {
                $('.formtable').submit();
            } else {
                swal('Your imaginary file is safe!');
            }
        });
}


// function Logout(url) {
//     swal({
//         title: 'Logout',
//         text: 'Apakah anda yakin untuk keluar ?',
//         showCancelButton:true,
//         allowOutsideClick:true,
//         closeOnConfirm: false,
//         confirmButtonColor: '#009be0',
//         confirmButtonText: 'Yakin',
//         cancelButtonText: 'Batal',
//         type: '',
//         html: true
//     }, function(){
//         window.location.href = url
//     });
// }

function Logout(url) {
    swal({
        title: 'Logout',
        text: 'Apakah anda yakin untuk keluar ?',
        icon: 'warning',
        buttons: true,
        dangerMode: true,
    })
        .then((willDelete) => {
            if (willDelete) {
                window.location.href = url
            }
        });
}

$("#swal-1").click(function () {
    swal('Hello');
});

