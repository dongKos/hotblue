$(function() {
	user();
	$('#userSearch').val('');

	$('#chkAllUser').click(function() {
		if ($('#chkAllUser').is(':checked')) $('input:checkbox[name=chkUser]').prop('checked', true);
		else $('input:checkbox[name=chkUser]').prop('checked', false)
	});
});

function user() {
	$.ajax({
		url: '/user',
		method: 'GET',
		data: { flag: 'N' },
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
		var keyword = $('#userSearch').val();
		var tbody = $('#userTable tbody');

		if (keyword.length == 0) {
			tbody.empty();
			user();
			return;
		} else if (keyword.length < $('#userSearch').attr('minlength') && keyword.length > 0) {
			alert('2글자 이상 입력해 주세요.');
			return;
		}

		tbody.empty();

		$.ajax({
			url: '/user/' + keyword,
			method: 'GET',
			data: { flag: 'N' },
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

function add_btn() {
	var chkUserId = [];
	var chkNickname = [];
	$('input:checkbox[name=chkUser]:checked').each(function(i) {
		chkUserId.push($(this).attr('id'));
		chkNickname.push($(this).attr('nickname'));
	});

	if (chkUserId.length < 1) {
		alert('사용자를 선택하세요.');
		return;
	}

	console.log('체크한 유저 : ' + chkUserId + ' , 체크한 유저 수 : ' + chkUserId.length);

	if (confirm(chkNickname + '님을 유투버디로 추가하시겠습니까?')) {
		var data = {
			userList: chkUserId,
			flag: 'Y'
		};

		$.ajax({
			url: '/user/updateYoutuberYn',
			method: 'POST',
			data: JSON.stringify(data),
			contentType: "application/json",
			success: function(result) {
				console.log(result);

				if (result.data) {
					//location.reload();
					location.href = '/admin/youtuber';
					alert(chkNickname + '님을 유투버디로 추가하였습니다.');
				} else {
					alert('유투버디 추가 실패하였습니다.');
				}
			},
			error: function(err) {
				alert('유투버디 추가 실패하였습니다.');
				console.log(err);
			}
		});
	}
}

function createTable(items) {
	var tbody = $('#userTable tbody');
	if (items.length > 0) {

		for (var i = 0; i < items.length; i++) {
			var html = "<tr>";
			html += "<td><div class='form-check check-box'>"
				+ "<input class='form-check-input' type='checkbox' value='' name='chkUser' id='" + items[i].userId + "' nickname='" + items[i].nickname + "' onclick='chk_user()'>"
				+ "</div></td>";
			html += "<td><p class='text-sm font-weight-bold mb-0 px-3'>" + items[i].userId + "</p></td>";
			html += "<td><p class='text-sm font-weight-bold mb-0'>" + items[i].nickname + "</p></td>";
			html += "<td><p class='text-sm font-weight-bold mb-0'>" + (items[i].username == null ? '' : items[i].username) + "</p></td>";
			html += "<td class='align-middle'><p class='text-sm font-weight-bold mb-0'>" + items[i].userMail + "</p></td>";
			html += "<td class='align-middle'><p class='text-sm font-weight-bold mb-0'>" + items[i].phoneNum + "</p></td>";
			html += "<td class='align-middle text-center'>"
				+ "<a class='btn btn-link text-danger text-gradient px-3 mb-0' id='" + items[i].userId + "' nickname='" + items[i].nickname + "' data-bs-target='#user-delete' data-bs-toggle='modal' onclick='delete_modal(this)'>"
				+ "<i class='far fa-trash-alt me-2' aria-hidden='true'></i>Delete"
				+ "</a>"
				+ "</td>";
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

function delete_modal(e) {
	var userId = $(e).attr('id');
	var nickname = $(e).attr('nickname');

	$('#deleteUserId').text(userId);
	$('#deleteNickname').text(nickname);
}

function delete_btn() {
	var nickname = $('#deleteNickname').text();

	if (confirm(nickname + '님을 회원 탈퇴 처리하시겠습니까?')) {
		$.ajax({
			url: '/user/' + $('#deleteUserId').text(),
			method: 'DELETE',
			success: function(result) {
				console.log(result);

				if (result.data) {
					location.reload();
					alert(nickname + '님을 회원 탈퇴 처리하였습니다.');
				} else {
					alert('회원 탈퇴 처리 실패하였습니다.');
				}
			},
			error: function(err) {
				alert('회원 탈퇴 처리 실패하였습니다.');
				console.log(err);
			}
		});
	}
}

function chk_user() {
	var totalCnt = $('input:checkbox[name=chkUser]').length;
	var chkCnt = $('input:checkbox[name=chkUser]:checked').length;

	if (totalCnt == chkCnt) $('#chkAllUser').prop('checked', true);
	else $('#chkAllUser').prop('checked', false);

}