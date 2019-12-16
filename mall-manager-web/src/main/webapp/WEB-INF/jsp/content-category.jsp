<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div>
	 <ul id="contentCategory" class="easyui-tree">
    </ul>
</div>
<div id="contentCategoryMenu" class="easyui-menu" style="width:120px;" data-options="onClick:menuHandler">
    <div data-options="iconCls:'icon-add',name:'add'">添加</div>
    <div data-options="iconCls:'icon-remove',name:'rename'">重命名</div>
    <div class="menu-sep"></div>
    <div data-options="iconCls:'icon-remove',name:'delete'">删除</div>
</div>
<script type="text/javascript">
//文档加载后处理以下逻辑
$(function(){
	$("#contentCategory").tree({
		url : '/content/category/list',
		animate: true,
		method : "GET",
		//右击鼠标才触发
		onContextMenu: function(e,node){
		    //关闭原来鼠标的默认事件
            e.preventDefault();
            //右击选中节点
            $(this).tree('select',node.target);
            //展示菜单
            $('#contentCategoryMenu').menu('show',{
                left: e.pageX, //在鼠标的位置显示
                top: e.pageY
            });
        },
        //在节点编辑之后触发
        onAfterEdit : function(node){
		    //获取树本身
        	var _tree = $(this);
        	//表示新增的节点
        	if(node.id == 0){
        		// 新增节点
        		$.post("/content/category/create",{parentId:node.parentId,name:node.text},function(data){
        			if(data.status == 200){
        			    //更新节点
        				_tree.tree("update",{
            				target : node.target,
            				id : data.data.id
            			});
        			}else{
        				$.messager.alert('提示','创建'+node.text+' 分类失败!');
        			}
        		});
        	}else{
        		$.post("/content/category/update",{id:node.id,name:node.text});
        	}
        }
	});
});
//处理点击菜单事件
function menuHandler(item){
    //获取树
	var tree = $("#contentCategory");
	//获取被选中节点
	var node = tree.tree("getSelected");
	//判断添加、重命名、删除
	if(item.name === "add"){
	    //点击添加触发
		tree.tree('append', {
            parent: (node?node.target:null),//被添加的子节点的父
            data: [{
                text: '新建分类',//节点名称
                id : 0,//节点ID
                parentId : node.id//父节点ID
            }]
        });
		//找到ID为0的节点
		var _node = tree.tree('find',0);
		//选中ID为0的节点，开始编辑
		tree.tree("select",_node.target).tree('beginEdit',_node.target);
	}else if(item.name === "rename"){
		tree.tree('beginEdit',node.target);
	}else if(item.name === "delete"){
		$.messager.confirm('确认','确定删除名为 '+node.text+' 的分类吗？',function(r){
			if(r){
				$.post("/content/category/delete/",{parentId:node.parentId,id:node.id},function(){
					tree.tree("remove",node.target);
				});	
			}
		});
	}
}
</script>