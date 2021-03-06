<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	

<!DOCTYPE html>

<html>
<head>
<meta charset="UTF-8"/>
<title>资源列表</title>
<link
	href="${baseURL}/ligerUI/lib/ligerUI/skins/Aqua/css/ligerui-all.css"
	rel="stylesheet" type="text/css" />

<link href="${baseURL}/ligerUI/lib/ligerUI/skins/ligerui-icons.css"
	rel="stylesheet" type="text/css" />
<script src="${baseURL}/ligerUI/lib/jquery/jquery-1.3.2.min.js"
	type="text/javascript"></script>
<script src="${baseURL}/ligerUI/lib/ligerUI/js/core/base.js"
	type="text/javascript"></script>
<script src="${baseURL}/ligerUI/lib/ligerUI/js/plugins/ligerLayout.js"
	type="text/javascript"></script>
<script src="${baseURL}/ligerUI/lib/jquery/jquery-1.3.2.min.js"
	type="text/javascript"></script>
<script src="${baseURL}/ligerUI/lib/ligerUI/js/core/base.js"
	type="text/javascript"></script>
<script src="${baseURL}/ligerUI/lib/json2.js"></script>
<script src="${baseURL}/ligerUI/lib/ligerUI/js/ligerui.all.js"
	type="text/javascript"></script>
<script src="${baseURL}/ligerUI/lib/ligerUI/js/plugins/ligerTab.js"></script>
<script src="${baseURL}/ligerUI/lib/jquery.cookie.js"></script>
<script src="${baseURL}/ligerUI/lib/ligerUI/js/plugins/ligerGrid.js"
	type="text/javascript"></script>
<script src="${baseURL}/ligerUI/lib/ligerUI/js/plugins/ligerTab.js"></script>
<script
	src="${baseURL}/ligerUI/lib/ligerUI/js/plugins/ligerResizable.js"
	type="text/javascript"></script>
<script src="${baseURL}/ligerUI/lib/ligerUI/js/plugins/ligerCheckBox.js"
	type="text/javascript"></script>

<script src="${baseURL}/ligerUI/lib/ligerUI/js/plugins/ligerForm.js"
	type="text/javascript"></script>
<script
	src="${baseURL}/ligerUI/lib/ligerUI/js/plugins/ligerDateEditor.js"
	type="text/javascript"></script>
<script src="${baseURL}/ligerUI/lib/ligerUI/js/plugins/ligerComboBox.js"
	type="text/javascript"></script>
<script src="${baseURL}/ligerUI/lib/ligerUI/js/plugins/ligerCheckBox.js"
	type="text/javascript"></script>
<script src="${baseURL}/ligerUI/lib/ligerUI/js/plugins/ligerButton.js"
	type="text/javascript"></script>
<script src="${baseURL}/ligerUI/lib/ligerUI/js/plugins/ligerDialog.js"
	type="text/javascript"></script>
<script src="${baseURL}/ligerUI/lib/ligerUI/js/plugins/ligerRadio.js"
	type="text/javascript"></script>
<script src="${baseURL}/ligerUI/lib/ligerUI/js/plugins/ligerSpinner.js"
	type="text/javascript"></script>
<script src="${baseURL}/ligerUI/lib/ligerUI/js/plugins/ligerTextBox.js"
	type="text/javascript"></script>
<script src="${baseURL }/ligerUI/lib/ligerUI/js/plugins/ligerDrag.js" type="text/javascript"></script>

<style type="text/css">
</style>

<script type="text/javascript">
	var grid = null;
	
	$(function() {
		grid = $("#maingrid")
				.ligerGrid(
						{
							columns : [ {
								display : 'name',
								name : 'name',
								width : 200,
								align: 'left'
							}, {
								display : 'url',
								name : 'url',
								width : 400,
								align:"left"
							}, {
								display : 'desc',
								width: 200,
								name : 'desc'
							} ],
							dataAction : "server",
							url : "${baseURL}/privilege/listJson.do",
							height : "98%",
							pageSize : 30,
							width : "98%",
							rownumbers : true,
							checkbox : true,
							autoCheckChildren: false,
							tree: { 
								columnName: 'name',
								treeLine:true,
								isExpand:false
							},
							toolbar : {
								items : [
										{
											text : '增加新类别',
											click : addResource,
											icon : 'add'
										},
										{
											line : true
										},
										{
											text : '增加子权限',
											click : addChildResource,
											icon : 'add'
										},
										{
											line : true
										},
										{
											text : '修改',
											click : updateResource,
											icon : 'modify'
										},
										{
											line : true
										},
										{
											text : '删除',
											click : deletePrivilege,
											img : '${baseURL}/ligerUI/lib/ligerUI/skins/icons/delete.gif'
										} ]
							}
						});

		$("#pageloading").hide();

		function addResource() {
			f_open();
		}
		
		function addChildResource(){
			var selectedRows = grid.getSelectedRows();
			if(selectedRows.length > 1){
				alert("不能同时选择多个父亲节点");
				return;
			}
			
            var parentId = selectedRows[0].id;
            
			if(!parentId){
				alert("请选择要添加的父亲结点");
				return;
			}
			
			if(parentId.indexOf(",") != -1){
				alert("不能同时选择多个父亲节点");
				return;
			}
			
			
			
			$.ligerDialog.open({
				height : 400,
				width : 600,
				title : '添加子模块权限',
				url : '${baseURL}/privilege/addChild.do?parentId='+parentId,
				showMax : false,
				showToggle : true,
				showMin : false,
				isResize : true,
				slide : false,
				data : {
					"grid" : grid
				}
			});
		}

		function f_open() {
			$.ligerDialog.open({
				height : 400,
				width : 600,
				title : '添加权限类别',
				url : '${baseURL}/privilege/add.do',
				showMax : false,
				showToggle : true,
				showMin : false,
				isResize : true,
				slide : false,
				data : {
					"grid" : grid
				}
			});
		}

		//delete selected row
		function deletePrivilege() {
			var ids = getSelectedRow();
			
			var url = "${baseURL}/privilege/delete.do?idsStr=" + ids;
			$.ajax({
				type : "POST",
				url : url,
				error : function(request) {
					alert("error");
				},
				success : function(data) {
					grid.reload(); //reload grid data
					$.ligerDialog.tip( //show delete success tip
					{
						title : '提示信息',
						content : '记录已经删除！'
					});
				}
			});
		}

		//get selected row id
		function getSelectedRow() {
			var selectedRows = grid.getSelectedRows();
			var ids = "";
			for ( var row in selectedRows) {
				ids += "," + selectedRows[row].id;
			}
			ids = ids.substring(1, ids.length);
			return ids;
		}

		function updateResource() {
			var id = getSelectedRow();
			if(id.indexOf(",") != -1){
				alert("不能同时更新多个权限");
				return;
			}
			
			if(!id || id.length==0){
				alert("请选择要更新的权限");
				return;
			}
			$.ligerDialog.open({
				height : 400,
				width : 600,
				title : '更新权限类别',
				url : '${baseURL}/privilege/edit.do?id=' + id,
				showMax : false,
				showToggle : true,
				showMin : false,
				isResize : true,
				slide : false,
				data : {
					"grid" : grid
				}
			});
		}
	});
	
</script>
</head>

<body>
	<div class="l-loading" style="display: block" id="pageloading"></div>

	<div class="l-clear"></div>

	<div id="maingrid"></div>

	<div style="display: none;"></div>
</body>
</html>
