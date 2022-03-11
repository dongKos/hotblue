$(function() {
	sortInit();

	recommended_youtuber();
	youtuber();
	$('#youtuberSearch').val('');

	$('#chkAllYoutber').click(function() {
		if ($('#chkAllYoutber').is(':checked')) $('input:checkbox[name=chkYoutuber]').prop('checked', true);
		else $('input:checkbox[name=chkYoutuber]').prop('checked', false)
	});
});

// 추천 유투버 순서 변경
function sortInit() {
	$("#sortable").sortable({
		placeholder: "",
		start: function(e, ui) {
			ui.item.data('start_pos', ui.item.index());
		},
		update: function(e, ui) {
			var start = ui.item.data('start_pos');
			var end = ui.item.index();
			reorder();			
		},
		stop: function(e, ui) {
			let data = new Map();
			$('#sortable tr').each(function(i, td) {
				var orderIdx = $(td).find('.a-order').attr('orderIdx');
				var orgOrderIdx = $(td).find('.a-order').attr('orgOrderIdx');
				if((orderIdx != orgOrderIdx) && (orderIdx != undefined)) {
					data.set($(td).find('.a-order').attr('id'), orderIdx);
				}
			});
			
			if(data.size == 0) {
				return;
			}
			
			console.log(Object.fromEntries(data));
			
			$.ajax({
				url: '/youtuber/updateOrderIdx',
				method: 'POST',
				data: JSON.stringify(Object.fromEntries(data)),
				contentType: 'application/json',
				success: function(result) {
					console.log(result);

					if (result.data) {
						$("#sortable").empty();
						recommended_youtuber();
					} else {
						location.reload();
						alert('순서 변경 실패하였습니다.');
					}
				},
				error: function(err) {
					location.reload();
					alert('순서 변경 실패하였습니다.');
					console.log(err);
				}
			});
		}
	}).disableSelection();
}

// 추천 유투버 순서 재정렬
function reorder() {
	$('#sortable tr').each(function(i, td) {
		$(td).find('.a-order').attr('orderIdx', i + 1);
	});
}

// 유투버 조회
function youtuber() {
	$.ajax({
		url: '/user',
		method: 'GET',
		data: { flag: 'Y' },
		success: function(data) {
			console.log(data);
			var items = data.data;
			createTable(items, '');
		},
		error: function(err) {
			console.log(err);
		}
	});
}

// 추천 유투버 조회
function recommended_youtuber() {
	$.ajax({
		url: '/youtuber/recommended',
		method: 'GET',
		success: function(data) {
			console.log(data);
			var items = data.data;
			$('#recommendedYoutuberBtn').attr('lastOrderIdx', items.length);
			createTable(items, 'recommended');
		},
		error: function(err) {
			console.log(err);
		}
	});
}

// 유투버 검색
function search_btn(e) {
	if (e.keyCode == 13) {
		var keyword = $('#youtuberSearch').val();
		var tbody = $('#youtuberTable tbody');

		if (keyword.length == 0) {
			tbody.empty();
			youtuber();
			return;
		} else if (keyword.length < $('#youtuberSearch').attr('minlength') && keyword.length > 0) {
			alert('2글자 이상 입력해 주세요.');
			return;
		}

		tbody.empty();

		$.ajax({
			url: '/user/' + keyword,
			method: 'GET',
			data: { flag: 'Y' },
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

// 추천 유투버 추가, 유투버 삭제
function youtuber_btn(val) {
	// 추천 유투버 수 제한
	if(val == 'add' && $('#recommendedYoutuberBtn').attr('lastOrderIdx') == 5) {
		alert('추천 유투버는 최대 5명까지 추가할 수 있습니다.');
		return;
	}

	var chkUserId = [];
	var chkNickname = [];
	
	$('input:checkbox[name=chkYoutuber]:checked').each(function(i) {
		var id = $(this).attr('id');
		var nickname = $(this).attr('nickname');
		if(chkRecYoutuber(id)) {
			if(val == 'add') {
				alert(nickname + '님은 이미 추천 유투버로 추가되어 있습니다.');
			} else {
				alert(nickname + '님을 추천 유투버에서 삭제해 주세요.');
			}
			$(this).prop('checked', false);
			chk_user();
		} else {
			chkUserId.push(id);
			chkNickname.push(nickname);
		}
	});

	if (chkUserId.length < 1) {
		alert('사용자를 선택하세요.');
		return;
	}

	console.log('체크한 유저 : ' + chkUserId + ' , 체크한 유저 수 : ' + chkUserId.length);

	var message = (val == 'add') ? '님을 추천 유투버로 추가하시겠습니까?' : '님을 유투버디에서 삭제하시겠습니까?';
	if (confirm(chkNickname + message)) {
		if (val == 'add') {
			var data = {
				userList: chkUserId,
				lastOrderIdx : $('#recommendedYoutuberBtn').attr('lastOrderIdx')
			}

			$.ajax({
				url: '/youtuber/recommended',
				method: 'POST',
				data: JSON.stringify(data),
				contentType: "application/json",
				success: function(result) {
					console.log(result);

					if (result.data) {
						location.reload();
						alert(chkNickname + '님을 추천 유투버로 추가하였습니다.');
					} else {
						alert('추천 유투버 추가 실패하였습니다.');
					}
				},
				error: function(err) {
					alert('추천 유투버 추가 실패하였습니다.');
					console.log(err);
				}
			});
		} else if (val == 'remove') {
			var data = {
				userList: chkUserId,
				flag: 'N'
			};

			$.ajax({
				url: '/user/updateYoutuberYn',
				method: 'POST',
				data: JSON.stringify(data),
				contentType: "application/json",
				success: function(result) {
					console.log(result);

					if (result.data) {
						location.reload();
						alert(chkNickname + '님을 유투버디에서 삭제하였습니다.');
					} else {
						alert('유투버디 삭제 실패하였습니다.');
					}
				},
				error: function(err) {
					alert('유투버디 삭제 실패하였습니다.');
					console.log(err);
				}
			});
		}
	}
}

// 추천 유투버인지 체크
function chkRecYoutuber(id) {
	var ch = false;
	
	$('#sortable tr').each(function(i, td) {
		console.log($(td).find('.a-order').attr('userId'));
		if($(td).find('.a-order').attr('userId') == id) {
			ch = true;
			return;
		}
	});
	
	return ch;
}

// 테이블 생성
function createTable(items, type) {
	var tbody = (type == 'recommended') ? $('#sortable') : $('#youtuberTable tbody');
	
	if (items.length > 0) {
		for (var i = 0; i < items.length; i++) {
			var html = "<tr>";
			if(type == 'recommended') {
				html += "<td>"
					+ "<a class='btn btn-link text-dark px-3 mb-0'>"
					+ "<i class='fas i-order " + createOrderIcon(items[i].orderIdx) + "'></i>"
					+ "</a>"
					+ "</td>";
				/*html += "<td><div class='align-items-center order-btn'>"
					+ "<button class='btn btn-icon-only btn-rounded btn-outline-danger mb-0 me-2 btn-sm align-items-center justify-content-center'>" + items[i].orderIdx + "</button>"
					+ "</div></td>";*/			
			} else {
				html += "<td><div class='form-check check-box'>"
					+ "<input class='form-check-input' type='checkbox' value='' name='chkYoutuber' id='" + items[i].userId + "' nickname='" + items[i].nickname + "' onclick='chk_user()'>"
					+ "</div></td>";
			}
			html += "<td><p class='text-sm font-weight-bold mb-0 px-3'>" + items[i].userId + "</p></td>";
			html += "<td><p class='text-sm font-weight-bold mb-0'>" + items[i].nickname + "</p></td>";
			html += "<td><p class='text-sm font-weight-bold mb-0'>" + (items[i].username == null ? '' : items[i].username) + "</p></td>";
			html += "<td class='align-middle'><p class='text-sm font-weight-bold mb-0'>" + items[i].userMail + "</p></td>";
			html += "<td class='align-middle'><p class='text-sm font-weight-bold mb-0'>" + items[i].phoneNum + "</p></td>";
			html += "<td class='align-middle text-center'>";
			if(type == 'recommended') {
				html += "<a class='btn btn-link text-danger text-gradient px-3 mb-0 a-order' id='" + items[i].recommendedYoutuberId + "' userId='" + items[i].userId + "' nickname='" + items[i].nickname + "' orgOrderIdx='" + items[i].orderIdx + "' onclick='delete_rec_btn(this)'>";
			} else {
				if(chkRecYoutuber(items[i].userId)) {					
					html += "<a class='btn btn-link text-danger text-gradient px-3 mb-0' onclick='alert(\"" + items[i].nickname + '님을 추천 유투버에서 삭제해 주세요.' + "\")'>";
				} else {					
					html += "<a class='btn btn-link text-danger text-gradient px-3 mb-0' id='" + items[i].userId + "' nickname='" + items[i].nickname + "' data-bs-target='#youtuber-delete' data-bs-toggle='modal' onclick='delete_modal(this)'>";
				}
			}
			html += "<i class='far fa-trash-alt me-2' aria-hidden='true'></i>Delete"
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
			+ "<span class='nav-link-text ms-2 text-sm'>데이터가 없습니다.</span>";
			/* + "<button class='btn btn-icon-only btn-rounded btn-outline-dark mb-0 me-2 btn-sm align-items-center justify-content-center'>"
			+ "<i class='fas fa-exclamation' aria-hidden='true'></i>"
			+ "</button>"
			+ "<span class='mb-1 text-dark font-weight-bold text-sm'>데이터가 없습니다.</span>"
			+ "</td>"; */
		html += "</td></tr>";

		tbody.append(html);
	}
}

// 주사위 아이콘 생성
function createOrderIcon(i) {
	var str = 'fa-dice-';
	switch(i) {
		case 1 : str += 'one'; break;
		case 2 : str += 'two'; break;
		case 3 : str += 'three'; break;
		case 4 : str += 'four'; break;
		case 5 : str += 'five'; break;
		default : str = ''; breeak;
	}
	
	return str;
}

function createList(items) {
	var ul = $('#sortable');
	if (items.length > 0) {

		for (var i = 0; i < items.length; i++) {
			var html = "<li class='list-group-item border-0 d-flex justify-content-between mb-2 bg-gray-100 border-radius-lg' id='" + items[i].recommendedYoutuberId + "' userId='" + items[i].userId + "'>";
			html += "<div class='d-flex align-items-center'>"
				+ "<button class='btn btn-icon-only btn-rounded btn-outline-dark mb-0 me-2 btn-sm align-items-center justify-content-center'>" + items[i].orderIdx + "</button>"
				+ "</div>";
			html += "<div class='d-flex align-items-center'>"
				+ "<h6 class='text-dark font-weight-bold text-sm mb-0'>" + items[i].nickname + "</h6>"
				+ "</div>";
			html += "<div class='d-flex align-items-center'>"
				+ "<h6 class='text-dark font-weight-bold text-sm mb-0'>" + items[i].username + "</h6>"
				+ "</div>";
			html += "<div class='d-flex align-items-center'>"
				+ "<h6 class='text-dark font-weight-bold text-sm mb-0'>" + items[i].userMail + "</h6>"
				+ "</div>";
			html += "<div class='d-flex align-items-center'>"
				+ "<h6 class='text-dark font-weight-bold text-sm mb-0'>" + items[i].phoneNum + "</h6>"
				+ "</div>";
			html += "<div class='d-flex align-items-center text-sm'>"
				+ "<a class='btn btn-link text-danger text-gradient px-3 mb-0' id='" + items[i].recommendedYoutuberId + "' nickname='" + items[i].nickname + "' onclick='delete_rec_btn(this)'>"
				+ "<i class='far fa-trash-alt me-2' aria-hidden='true'></i>Delete"
				+ "</a>"
				+ "</div>";
			html += "</li>";

			ul.append(html);
		}
	} else {
		var html = "<li class='list-group-item border-0 p-4 mb-2 bg-gray-100 border-radius-lg'>";
		html += "<div class='align-items-center text-sm text-center'>"
			+ "<button class='btn btn-icon-only btn-rounded btn-outline-dark mb-0 me-2 btn-sm align-items-center justify-content-center'>"
			+ "<i class='fas fa-exclamation' aria-hidden='true'/>"
			+ "</button>"
			+ "<span class='mb-1 text-dark font-weight-bold text-sm'>데이터가 없습니다.</span>"
			+ "</div>";
		html += "</li>";

		ul.append(html);
	}
}

// 회원 탈퇴 모달
function delete_modal(e) {
	$('#deleteYoutuberId').text($(e).attr('id'));
	$('#youtuberNickname').text($(e).attr('nickname'));
}

// 회원 탈퇴
function delete_btn() {
	var nickname = $('#youtuberNickname').text();

	if (confirm(nickname + '님을 회원 탈퇴 처리하시겠습니까?')) {
		$.ajax({
			url: '/user/' + $('#deleteYoutuberId').text(),
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

// 추천 유투버 삭제
function delete_rec_btn(e) {
	var nickname = $(e).attr('nickname');
	if (confirm(nickname + '님을 추천 유투버에서 삭제하시겠습니까?')) {
		var data = {
			recommendedYoutuberId: $(e).attr('id'),
			orderIdx : $(e).attr('orgOrderIdx'),
			lastOrderIdx : $('#recommendedYoutuberBtn').attr('lastOrderIdx')
		}
			
		$.ajax({
				url: '/youtuber/recommended',
				method: 'DELETE',
				data: JSON.stringify(data),
				contentType: "application/json",
				success: function(result) {
					console.log(result);

					if (result.data) {
						location.reload();
						alert(nickname + '님을 추천 유투버에서 삭제하였습니다.');
					} else {
						alert('추천 유투버 삭제 실패하였습니다.');
					}
				},
				error: function(err) {
					alert('추천 유투버 삭제 실패하였습니다.');
					console.log(err);
				}
			});
	}
}

// 체크 박스
function chk_user() {
	var totalCnt = $('input:checkbox[name=chkYoutuber]').length;
	var chkCnt = $('input:checkbox[name=chkYoutuber]:checked').length;

	if (totalCnt == chkCnt) $('#chkAllYoutber').prop('checked', true);
	else $('#chkAllYoutber').prop('checked', false);
}