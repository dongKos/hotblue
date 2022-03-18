$(function() {

	getProducts();
	$('#chkAllUser').click(function() {
		if ($('#chkAllUser').is(':checked')) $('input:checkbox[name=chkUser]').prop('checked', true);
		else $('input:checkbox[name=chkUser]').prop('checked', false)
	});

	$('#file').on('change', excelUpload);
	
	$('.search_area input').on('keyup', function(e) {
		if(e.keyCode == 13) getProducts();
	});
	$('#searchBtn').on('click', function() {
		getProducts();
	});
	$('#saveBtn').on('click', function() {
		saveProduct();
	})
});

function getProducts() {
	var data = {
		'keyword': $("#schKeyword").val(),
		'shopName': $('#schShopName').val(),
		'nvmid': $('#schNvmid').val(),
		'memo': $('#schMemo').val()
	};
	$.ajax({
		type: "POST",
		url: "/product",
		data: data,
		success: function(data) {
			console.log(data);
			if (data.data != null)
				initPrdData(data.data);
		},
		error: function(err) {
			console.log("err:", err)
		}
	});
}

function saveProduct() {
	var confirm = window.confirm("등록하시겠습니까?");
	if(!confirm) return;
	var data = {
		'keyword': $("#saveKeyword").val(),
		'onebuOptionName': $('#saveOnebuOptionName').val(),
		'nvmid': $('#saveNvmid').val(),
		'memo': $('#saveMemo').val()
	};
	$.ajax({
		type: "POST",
		url: "/product/save",
		data: data,
		success: function(data) {
			console.log(data);
			if (data.code == '200')
				getProducts();
		},
		error: function(err) {
			console.log("err:", err)
		}
	});
}

function excelUpload() {
	$.ajax({
		type: "POST",
		url: "/product/excelUpload",
		contentType: false,
		processData: false,
		enctype: 'multipart/form-data',
		data: new FormData($('#form')[0]),
		success: function(data) {
			console.log(data);
			if(data.code == 200) {
				alert("성공");
				location.reload();
			}
		},
		error: function(err) {
			console.log("err:", err)
		}
	});
}

function dtForm(date) {
	if(date == null || date.length == 0)
		return "";
	var ymd = date.split('T')[0];
	var m = ymd.split('-')[1];
	var d = ymd.split('-')[2];
	var time = date.split('T')[1].split(".")[0];
	var result = m + "-" + d + " " + time;
	return result;
}

function initPrdData(items) {
	var tbody = $('#prdTable tbody');
	$(tbody).html('');
	if (items.length > 0) {
		var idx = 1;
		for (var i = 0; i < items.length; i++) {
			var classNm = "";
			if(items[i].onebuRank == 0 && items[i].onebuInnerRank == 0 && items[i].prdRank == 0) {
				classNm = "work-fail";
			}
			var href = "";
			if(items[i].singlePrdLink == '' || items[i].singlePrdLink == null) {
				href=items[i].onebuLink;
			} else {
				href=items[i].singlePrdLink;
			}
				
			var html = "<tr class='" + classNm + "'>";
			html += "<td style='display: none;' name='id'>" + items[i].id + "</td>";
			html += "<td><p class='text-sm font-weight-bold mb-0 px-3'>" + idx++ + "</p></td>";
			html += "<td><a class='text-sm font-weight-bold mb-0 px-3' href='" + href + "' target='_blank'>" + items[i].keyword + "</a></td>";
			html += "<td><p class='text-sm font-weight-bold mb-0'>" + items[i].shopName + "</p></td>";
			html += "<td><p class='text-sm font-weight-bold mb-0'>" + items[i].onebuOptionName + "</p></td>";
			html += "<td class='text-center'><p class='text-sm font-weight-bold mb-0'>" + items[i].nvmid + "</p></td>";
			html += "<td class='text-center'><p class='text-sm font-weight-bold mb-0 pointer'>" + commonUtil.getRankText(items[i].prvOnebuRank, items[i].onebuRank) + "</p></td>";
			html += "<td class='text-center'><p class='text-sm font-weight-bold mb-0 pointer'>" + commonUtil.getRankText(items[i].prvOnebuInnerRank, items[i].onebuInnerRank) + "</p></td>";
			html += "<td class='text-center'><p class='text-sm font-weight-bold mb-0 pointer'>" + commonUtil.getRankText(items[i].prvPrdRank, items[i].prdRank) + "</p></td>";
			html += "<td class='text-center align-middle'><p class='text-sm font-weight-bold mb-0'>" + dtForm(items[i].workDate) + "</p></td>";
			html += "<td class='align-middle'><p class='text-sm font-weight-bold mb-0 pointer' name='memo' style='color : #cb0c9f;'>" + ((items[i].memo == null || items[i].memo == '')?"없음":items[i].memo) + "</p></td>";
			html += "<td class='align-middle'><button type='button' style='margin-bottom: 0; padding: 0.25rem 0.5rem;' class='btn btn-danger' onclick='deleteProduct(" + items[i].id+ ");'>삭제</button></td>";
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
	
	$('.pointer').on('click', function() {
		var name = $(this).attr('name');
		if(name == 'memo') {
			$('#modalBtn').click();
			initDetail($(this).parent().siblings('td[name="id"]').text());
		} else {
			$('#modalBtn2').click();
			initHistoryDetail($(this).parent().siblings('td[name="id"]').text());
		}
	});
}

function initHistoryData(items) {
	console.log(items);
	var tbody = $('#historyTable tbody');
	$(tbody).html('');
	if (items.length > 0) {
		var idx = 1;
		for (var i = 0; i < items.length; i++) {
				
			var html = "<tr>";
			html += "<td class='text-center'><p class='text-sm font-weight-bold mb-0 pointer'>" + items[i].onebuRank + "</p></td>";
			html += "<td class='text-center'><p class='text-sm font-weight-bold mb-0 pointer'>" + items[i].onebuInnerRank + "</p></td>";
			html += "<td class='text-center'><p class='text-sm font-weight-bold mb-0 pointer'>" + items[i].prdRank + "</p></td>";
			html += "<td class='text-center align-middle'><p class='text-sm font-weight-bold mb-0'>" + dtForm(items[i].workDate) + "</p></td>";
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

function initDetail(id) {
	$.ajax({
		url: "/product/detail/"+id,
		success: function(data) {
			console.log(data);
			$('#modalId').val(data.data.id);
			$('#modelKeyword').val(data.data.keyword);
			$('#modelOnebuOptionName').val(data.data.onebuOptionName);
			$('#modalNvmid').val(data.data.nvmid);
			$('#modalMemo').val(data.data.memo);
		},
		error: function(err) {
			console.log("err:", err)
		}
	});
}

function initHistoryDetail(id) {
	console.log(id);
	$.ajax({
		url: "/history/"+id,
		success: function(data) {
			console.log(data);
			if(data.data != null) {
				initHistoryData(data.data);
			}
		},
		error: function(err) {
			console.log("err:", err)
		}
	});
}
function updateProduct() {
	var confirm = window.confirm("수정하시겠습니까?");
	if(!confirm) return;
	var data = {
		id: $('#modalId').val().trim(),
		keyword: $('#modelKeyword').val().trim(),
		onebuOptionName: $('#modelOnebuOptionName').val().trim(),
		nvmid: $('#modalNvmid').val().trim(),
		memo: $('#modalMemo').val().trim()
	};
	$.ajax({
		type: "PUT",
		url: "/product",
		data: JSON.stringify(data),
		contentType: 'application/json; charset=UTF-8',
		success: function(data) {
			console.log(data);
			getProducts();
		},
		error: function(err) {
			console.log("err:", err)
		}
	});
	$('#modalClose').click();
}

function deleteProduct(id) {
	var confirm = window.confirm("삭제하시겠습니까?");
	if(!confirm) return;
	$.ajax({
		type: "DELETE",
		url: "/product/delete/"+id,
		success: function(data) {
			console.log(data);
			getProducts();
		},
		error: function(err) {
			console.log("err:", err)
		}
	});
}