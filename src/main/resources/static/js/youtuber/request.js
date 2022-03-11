$(function() {
	request();
	$('#requestSearch').val('');

	$('#chkAllRequest').click(function() {
		if ($('#chkAllRequest').is(':checked')) $('input:checkbox[name=chkRequest]').prop('checked', true);
		else $('input:checkbox[name=chkRequest]').prop('checked', false)
	});
});

function request() {
	var data = {
		userMail: $('#userMail').val()
	};

	$.ajax({
		url: '/buddyRequest/findAll',
		method: 'POST',
		data: JSON.stringify(data),
		contentType: "application/json",
		success: function(data) {
			console.log(data);
			var items = data.data;
			createTable(items);
		},
		error: function(err) {
			console.log(err);
		}
	});
}

function search_btn(e) {
	if (e.keyCode == 13) {
		var keyword = $('#requestSearch').val();
		var tbody = $('#requestTable tbody');

		if (keyword.length == 0) {
			tbody.empty();
			request();
			return;
		} else if (keyword.length < $('#requestSearch').attr('minlength') && keyword.length > 0) {
			alert('2글자 이상 입력해 주세요.');
			return;
		}

		tbody.empty();
		/*$.ajax({
			url: '/buddyRequest/' + keyword,
			method: 'GET',
			success: function(data) {
				console.log(data);
				var items = data.data;
				createTable(items);
			},
			error: function(err) {
				console.log(err);
			}
		});*/
	}
}

function accept_btn() {

}

function createTable(items) {
	var tbody = $('#requestTable tbody');
	if (items.length > 0) {

		for (var i = 0; i < items.length; i++) {
			var html = "<tr>";
			html += "<td class='check-box'><div class='form-check'>"
				+ "<input class='form-check-input' type='checkbox' value='' name='chkRequest' id='" + items[i].buddyRequestId + "' nickname='" + items[i].nickname + "' onclick='chk_request()'>"
				+ "</div></td>";
			html += "<td><p class='text-sm font-weight-bold mb-0 px-3'>" + (i + 1) + "</p></td>";
			html += "<td><p class='text-sm font-weight-bold mb-0'>" + items[i].nickname + "</p></td>";
			html += "<td class='align-middle'><p class='text-sm font-weight-bold mb-0'>" + items[i].userMail + "</p></td>";
			html += "<td class='align-middle'><p class='text-sm font-weight-bold mb-0 greeting-text' id='request" + items[i].buddyRequestId + "'>" + items[i].greetings + "</p></td>";
			html += "<td class='align-middle text-center'>"
				+ "<a class='btn btn-link pe-3 ps-0 mb-0 ms-auto' id='" + items[i].buddyRequestId + "' data-bs-target='#show-request' data-bs-toggle='modal' onclick='show_modal(this)'>Message"
				//+ "<i class='ni ni-bell-55 text-success text-gradient me-2'></i>Delete"
				+ "</a>"
				+ "</td>";
			html += "</tr>";

			tbody.append(html);
		}
	} else {
		var html = "<tr>";
		html += "<td class='align-middle text-center no-data' colspan='6'>"
			+ "<svg class='text-dark' width='16px' height='16px' viewBox='0 0 40 44' version='1.1' xmlns='http://www.w3.org/2000/svg' xmlns:xlink='http://www.w3.org/1999/xlink'>"
			+ "<title>document</title>"
			+ "<g id='Basic-Elements' stroke='none' stroke-width='1' fill='none' fill-rule='evenodd'>"
			+ "<g id='Rounded-Icons' transform='translate(-1870.000000, -591.000000)' fill='#FFFFFF' fill-rule='nonzero'>"
			+ "<g id='Icons-with-opacity' transform='translate(1716.000000, 291.000000)'>"
			+ "<g id='document' transform='translate(154.000000, 300.000000)'>"
			+ "<path class='color-background' d='M40,40 L36.3636364,40 L36.3636364,3.63636364 L5.45454545,3.63636364 L5.45454545,0 L38.1818182,0 C39.1854545,0 40,0.814545455 40,1.81818182 L40,40 Z' id='Path' opacity='0.603585379'/>"
			+ "<path class='color-background' d='M30.9090909,7.27272727 L1.81818182,7.27272727 C0.814545455,7.27272727 0,8.08727273 0,9.09090909 L0,41.8181818 C0,42.8218182 0.814545455,43.6363636 1.81818182,43.6363636 L30.9090909,43.6363636 C31.9127273,43.6363636 32.7272727,42.8218182 32.7272727,41.8181818 L32.7272727,9.09090909 C32.7272727,8.08727273 31.9127273,7.27272727 30.9090909,7.27272727 Z M18.1818182,34.5454545 L7.27272727,34.5454545 L7.27272727,30.9090909 L18.1818182,30.9090909 L18.1818182,34.5454545 Z M25.4545455,27.2727273 L7.27272727,27.2727273 L7.27272727,23.6363636 L25.4545455,23.6363636 L25.4545455,27.2727273 Z M25.4545455,20 L7.27272727,20 L7.27272727,16.3636364 L25.4545455,16.3636364 L25.4545455,20 Z' id='Shape'/>"
			+ "</g></g></g></g></svg>"
			+ "<span class='nav-link-text ms-2 text-sm'>데이터가 없습니다.</span>"
			+ "</td>";
		html += "</tr>";

		tbody.append(html);
	}
}

function show_modal(e) {
	var buddyRequestId = $(e).attr('id');
	$('#greetings').val($('#request' + buddyRequestId).text());
}

function chk_request() {
	var totalCnt = $('input:checkbox[name=chkRequest]').length;
	var chkCnt = $('input:checkbox[name=chkRequest]:checked').length;

	if (totalCnt == chkCnt) $('#chkAllRequest').prop('checked', true);
	else $('#chkAllRequest').prop('checked', false);

}