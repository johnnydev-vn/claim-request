const date = document.getElementById("date");
const day = document.getElementById("day");
const from = document.getElementById("fromTime");
const to = document.getElementById("toTime");
const hours = document.getElementById("hours");
const remarks = document.getElementById("remarks");
let deletedDetailIds = [];

// Get day value from selected date
date.addEventListener("change", function () {
    const dateValue = new Date(this.value);
    const options = {weekday: 'short'};
    document.getElementById("day").value = dateValue.toLocaleDateString('en-US', options);
});

// Update total working hour
function calculateTotalWorkingHour() {
    let sum = 0;
    $('.countRow').each(function () {
        let value = parseFloat($(this).find('td:nth-child(12)').text()) || 0;
        sum += value;
    });

    $('#workingHourDisplay').text(sum + " hour(s)");
    $('#inputWorkingHours').val(sum);
}

// Add ClaimDetail
function addClaimDetail() {

    // Get form values
    const dateValue = date.value;
    const dayValue = day.value;
    const fromValue = from.value;
    const toValue = to.value;
    const hoursValue = hours.value;
    const remarksValue = remarks.value;

    // Create a new row
    let newRow = `
    <tr class="countRow">
        <td hidden=""><input type="text" name="claimDetailsList[${claimDetailsCount}].id" value="" /></td>
        <td hidden=""><input type="date" name="claimDetailsList[${claimDetailsCount}].dateClaim" value="${dateValue}" /></td>
        <td hidden=""><input type="text" name="claimDetailsList[${claimDetailsCount}].day" value="${dayValue}" /></td>
        <td hidden=""><input type="time" name="claimDetailsList[${claimDetailsCount}].fromTime" value="${fromValue}" /></td>
        <td hidden=""><input type="time" name="claimDetailsList[${claimDetailsCount}].toTime" value="${toValue}" /></td>
        <td hidden=""><input type="number" name="claimDetailsList[${claimDetailsCount}].totalNoOfHours" value="${hoursValue}" /></td>
        <td hidden=""><input type="text" name="claimDetailsList[${claimDetailsCount}].remarksDetails" value="${remarksValue}" /></td>
        <td>${dateValue}</td>
        <td>${dayValue}</td>
        <td>${fromValue}</td>
        <td>${toValue}</td>
        <td>${hoursValue}</td>
        <td>${remarksValue}</td>
        <td><button type="button" class="btn btn-danger btn-link removeDetailBtn"><i class="fa fa-times fs-4"></i></button></td>
    </tr>
  `;

    $('#claimDetailsTable').append(newRow);

    claimDetailsCount++;
    calculateTotalWorkingHour();
};

// Delete ClaimDetail
$(document).on('click', '.removeDetailBtn', function () {
    let row = $(this).closest('tr');
    let detailId = row.find('td:first-child input').val();

    // If the row has an ID (existing detail), mark it for deletion
    if (detailId) {
        console.log(detailId);
        deletedDetailIds.push(detailId);
        $('#deletedDetailIds').val(deletedDetailIds.join('-'));
    }
    row.remove();

    //Re-index details
    $('#claimDetailsTable tr').each(function (index) {
        $(this).find('td input').each(function () {
            let name = $(this).attr('name');
            if (name) {
                // Update the name attribute with the new index
                let newName = name.replace(/\[\d+\]/, '[' + index + ']');
                $(this).attr('name', newName);
            }
        });
    });

    calculateTotalWorkingHour();
});

$('#projectDropdown').change(function () {
    getStaffProjectInfo();
});

// Get StaffProject Info
function getStaffProjectInfo() {
    var selectedProjectId = $('#projectDropdown option:selected').val();
    if (!selectedProjectId) {
        selectedProjectId = $('#projectIdView').text();
    }

    $.ajax({
        url: '/claim/updateProjectInfo',
        type: 'POST',
        data: {
            selectedProject: selectedProjectId,
        },
        success: function (response) {
            $('#role').html(response.role);
            $('#duration').html(response.duration + " day(s)");
            $('#date')
                .attr("min", response.startDate)
                .attr("max", response.endDate);
        },
        error: function(xhr, status, error) {
            console.log("Error:", error);
        }
    });
}

// Valid add more form
document.getElementById("addRow").addEventListener("click", function () {
    let fromTime = $('#fromTime').val();
    let toTime = $('#toTime').val();
    let date = $('#date').val();
    let hours = $('#hours').val();
    let isValid = true;

    // Reset validation states
    $('#fromTime, #toTime, #date, #hours').removeClass('is-invalid is-valid');
    $('.invalid-feedback, .valid-feedback').hide();

    // Validate Date Input (required)
    if (!date) {
        $('#date').addClass('is-invalid');
        $('#date').siblings('.invalid-feedback').show();
        isValid = false;
    } else {
        $('#date').addClass('is-valid');
    }

    // Validate From Time (required and must not be later than To Time)
    if (!fromTime) {
        $('#fromTime').addClass('is-invalid');
        $('#fromTime').siblings('.invalid-feedback').show();
        isValid = false;
    } else if (fromTime && toTime && fromTime > toTime) {
        $('#fromTime').addClass('is-invalid');
        $('#fromTime').siblings('.invalid-feedback').show();
        isValid = false;
    } else {
        $('#fromTime').addClass('is-valid');
    }

    // Validate To Time (required and must not be earlier than From Time)
    if (!toTime) {
        $('#toTime').addClass('is-invalid');
        $('#toTime').siblings('.invalid-feedback').show();
        isValid = false;
    } else if (fromTime && toTime && toTime < fromTime) {
        $('#toTime').addClass('is-invalid');
        $('#toTime').siblings('.invalid-feedback').show();
        isValid = false;
    } else {
        $('#toTime').addClass('is-valid');
    }

    // Validate Hour Number (required and must be a valid number)
    if (!hours) {
        $('#hours').addClass('is-invalid');
        $('#hours').siblings('.invalid-feedback').show();
        isValid = false;
    } else if (isNaN(hours) || hours < 0) {
        $('#hours').addClass('is-invalid');
        $('#hours').siblings('.invalid-feedback').show();
        isValid = false;
    } else {
        $('#hours').addClass('is-valid');
    }

    if (isValid) {
        addClaimDetail();
        $('#close').click();
        $('#reset').click();
        $('#date, #fromTime, #toTime, #hours ').removeClass('is-valid');
    }
});

$(document).ready(function () {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    $(document).ajaxSend(function (e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });

    getStaffProjectInfo();
});

