$(function() {
	notice();

	$('#noticeBtn').on('click', function() {
		add_notice();
	});
});

function notice() {
	$.ajax({
		url: '/notice',
		method: 'GET',
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
		var keyword = $('#noticeSearch').val();
		var tbody = $('#noticeTable tbody');

		if (keyword.length == 0) {
			tbody.empty();
			notice();
			return;
		} else if (keyword.length < $('#noticeSearch').attr('minlength') && keyword.length > 0) {
			alert('2글자 이상 입력해 주세요.');
			return;
		}

		tbody.empty();

		$.ajax({
			url: '/notice/' + keyword,
			method: 'GET',
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
}

function add_notice() {
	var title = $('#title').val();
	var url = $('#url').val();

	if (!inputLengthChk(title, url)) return;

	if (url.indexOf('https://') == -1) url = 'https://' + url;

	if (confirm('공지사항을 등록하시겠습니까?')) {
		var data = {
			title: title,
			url: url,
			userId: $('#userMail').val(),
			username: $('#username').val()
		};

		$.ajax({
			url: '/notice',
			method: 'POST',
			data: JSON.stringify(data),
			contentType: "application/json",
			success: function(result) {
				console.log(result);

				if (result.data) {
					location.reload();
					alert('공지사항을 등록하였습니다.');
				} else {
					alert('공지사항 등록 실패하였습니다.');
				}
			},
			error: function(err) {
				alert('공지사항 등록 실패하였습니다.');
				console.log(err);
			}
		});
	}
}

function delete_btn(e) {
	if (confirm('공지사항을 삭제하시겠습니까?')) {
		$.ajax({
			url: '/notice/' + $(e).attr('id'),
			method: 'DELETE',
			success: function(result) {
				console.log(result);

				if (result.data) {
					location.reload();
					alert('공지사항을 삭제하였습니다.');
				} else {
					alert('공지사항 삭제 실패하였습니다.');
				}
			},
			error: function(err) {
				alert('공지사항 삭제 실패하였습니다.');
				console.log(err);
			}
		});
	}
}

function edit_modal(e) {
	var noticeId = $(e).attr('id');
	var title = 'title' + $(e).attr('num');
	var url = 'url' + $(e).attr('num');

	$('#noticeId').val(noticeId);
	$('#titleEdit').val($('#' + title).text());
	$('#urlEdit').val($('#' + url).text());
}

function edit_btn() {
	var title = $('#titleEdit').val();
	var url = $('#urlEdit').val();

	if (!inputLengthChk(title, url)) return;

	if (url.indexOf('https://') == -1) url = 'https://' + url;

	if (confirm('수정사항을 저장하시겠습니까?')) {
		var data = {
			noticeId: $('#noticeId').val(),
			title: title,
			url: url
		};

		$.ajax({
			url: '/notice',
			method: 'PUT',
			data: JSON.stringify(data),
			contentType: "application/json",
			success: function(result) {
				console.log(result);

				if (result.data) {
					location.reload();
					alert('공지사항을 수정하였습니다.');
				} else {
					alert('공지사항 수정 실패하였습니다.');
				}
			},
			error: function(err) {
				alert('공지사항 수정 실패하였습니다.');
				console.log(err);
			}
		});
	}
}

function createTable(items) {
	var tbody = $('#noticeTable tbody');
	if (items.length > 0) {

		for (var i = 0; i < items.length; i++) {
			var num = (i + 1);
			var html = "<tr>";
			html += "<td><p class='text-sm font-weight-bold mb-0 px-3'>" + num + "</p></td>";
			html += "<td><p class='text-sm font-weight-bold mb-0' id='title" + num + "'>" + items[i].title + "</p></td>";
			html += "<td><p class='text-sm font-weight-bold mb-0 notice-url' id='url" + num + "' onclick='window.open(\"" + items[i].url + "\")'>" + items[i].url + "</p></td>";
			html += "<td class='align-middle'><p class='text-sm font-weight-bold mb-0'>" + items[i].username + "</p></td>";
			html += "<td class='align-middle'><i class='far fa-calendar-alt me-2' aria-hidden='true'></i><span class='text-sm font-weight-bold mb-0'>" + items[i].createDate.substring(0, 10) + ' ' + items[i].createDate.substring(11, 16) + "</span></td>";
			if ($('#userMail').val() == items[i].userId) {
				html += "<td class='align-middle text-center'>"
					+ "<a class='btn btn-link text-dark px-3 mb-0' id='" + items[i].noticeId + "' num='" + num + "' data-bs-target='#notice-edit' data-bs-toggle='modal' onclick='edit_modal(this)'>"
					+ "<i class='fas fa-pencil-alt text-dark me-2' aria-hidden='true'></i>Edit"
					+ "</a></td>"
					+ "<td class='align-middle text-center'>"
					+ "<a class='btn btn-link text-danger text-gradient px-3 mb-0' id='" + items[i].noticeId + "' onclick='delete_btn(this)'>"
					+ "<i class='far fa-trash-alt me-2' aria-hidden='true'></i>Delete"
					+ "</a></td>";
			} else {
				html += "<td></td><td></td>";
			}
			html += "</tr>";

			tbody.append(html);
		}
	} else {
		var html = "<tr>";
		html += "<td class='align-middle text-center no-data' colspan='7'>"
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

function inputLengthChk(title, url) {
	if (title.length < $('#title').attr('minlength') && url.length < $('#url').attr('minlength')) {
		alert('제목과 게시글 주소를 2글자 이상 입력해 주세요.');
		return false;
	} else if (title.length < $('#title').attr('minlength')) {
		alert('제목을 2글자 이상 입력해 주세요.');
		return false;
	} else if (url.length < $('#url').attr('minlength')) {
		alert('게시글 주소를 2글자 이상 입력해 주세요.');
		return false;
	}

	return true;
}