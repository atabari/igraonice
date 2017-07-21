/**
 * Created by User on 2/24/2016.
 */
var apId = $('#apId').val();

var obavijest = "Trenutno nema zauzetih termina";
var time, pkg, myresponse;
var extendedTimesAsNumbers = [];

function pkgDuration(duration, pkgId) {
    time = duration;
    pkg = pkgId;
    $('#paketId').val(pkg);
}

$('#checkIn').change(function() {
    var date = $('#checkIn').val();
    var dateApId = date +"-"+ apId;
    $.ajax({
        data:dateApId,
        type: "GET",
        url: "/ajax/" + dateApId
    }).success(function(response) {
        if(response == 'null') {
            $('#datumi').text(obavijest)
        } else {
            // $('#datumi').text(response)
            myresponse = response;

            myresponse = myresponse.replace("[", "");
            myresponse = myresponse.replace("]", "");

            var times = myresponse.split(", ");

            var timesAsStrings = [];
            for (var i = 0; i < times.length; i++) {
                var n1 = times[i].split("-");
                var n1 = times[i].split("-");
                timesAsStrings = timesAsStrings.concat(n1);
            }

            var timesAsNumbers = [];
            for (var j = 0; j < timesAsStrings.length; j++) {
                timesAsNumbers.push(Number(timesAsStrings[j]));
            }
            timesAsNumbers.sort(function(a, b){return a-b});

            var poruka = "| ";
            for (var n = 0; n < timesAsNumbers.length; n += 2) {
                poruka = poruka.concat(timesAsNumbers[n], '-', timesAsNumbers[n+1] + 'h | ');
            }

            $('#datumi').text(poruka)
            for (var k = 0; k < timesAsNumbers.length; k += 2) {
                var l = timesAsNumbers[k];
                var m = timesAsNumbers[k+1];
                for (l; l < m; l++) {
                    extendedTimesAsNumbers.push(l);
                }
            }
        }
    });
});

// //var time = $('.time').val();
// var timeFromCheck = $('#timeFromCheck').val();
// var timeToCheck = $('#timeToCheck').val();
//
// $('#timeFrom').change(function() {
//
//     var timeFrom = $('#timeFrom').val();
//     var timeTo = (1*timeFrom) + (1*time);
//     $('#timeTo').val(timeTo)
//     //$('#paketId').val(pkg);
//     if(timeFrom < timeFromCheck || timeTo > timeToCheck) {
//         $('#confirmButton').prop('disabled', true);
//         $('#time_error').text("Odabrani termini nisu u okviru radnog vremena!")
//     } else {
//         $('#confirmButton').prop('disabled', false);
//         $('#time_error').text(" ");
//     }
//
//     if(extendedTimesAsNumbers.indexOf(Number(timeFrom)) > -1 || extendedTimesAsNumbers.indexOf(Number(timeTo)) > -1) {
//         $('#confirmButton').prop('disabled', true);
//         $('#time_error').text("Odabrani termin je već zauzet.")
//     } else {
//         $('#confirmButton').prop('disabled', false);
//     }
// });

var timeFromCheck = $('#timeFromCheck').val();
var timeToCheck = $('#timeToCheck').val();

$('#timeFrom').change(function() {
    var timeFrom = $('#timeFrom').val();
    var timeTo = (1*timeFrom) + (1*time);
    $('#timeTo').val(timeTo)
    //$('#paketId').val(pkg);
    if(timeFrom < timeFromCheck && timeTo > timeToCheck) {
        $('#time_error').text("Odabrani termini nisu u okviru radnog vremena!")
    } else {
        $('#time_error').text(" ");
    }

    if(extendedTimesAsNumbers.indexOf(Number(timeFrom)) > -1 || extendedTimesAsNumbers.indexOf(Number(timeTo)) > -1) {
        $('#confirmButton').prop('disabled', true);
        $('#time_error').text("Odabrani termin je već zauzet.")
    } else {
        $('#confirmButton').prop('disabled', false);
    }
});