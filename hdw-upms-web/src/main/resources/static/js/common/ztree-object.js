/**
 * ztree插件的封装
 */
(function() {

	var $ZTree = function(id, url) {
		this.id = id;
		this.url = url;
		this.onClick = null;
		this.settings = null;
		this.ondblclick=null;
	};

	$ZTree.prototype = {
		/**
		 * 初始化ztree的设置
		 */
		initSetting : function() {
			var settings = {
				view : {
					dblClickExpand : true,
					selectedMulti : false,
                    addDiyDom: this.addDiyDomWithCheck
				},
				data : {simpleData : {enable : true}},
				callback : {
					onClick : this.onClick,
					onDblClick:this.ondblclick
				}
			};
			return settings;
		},

		/**
		 * 手动设置ztree的设置
		 */
		setSettings : function(val) {
			this.settings = val;
		},

		/**
		 * 初始化ztree
		 */
		init : function() {
			var zNodeSeting = null;
			if(this.settings != null){
				zNodeSeting = this.settings;
			}else{
				zNodeSeting = this.initSetting();
			}
			var zNodes = this.loadNodes();
			$.fn.zTree.init($("#" + this.id), zNodeSeting, zNodes);
		},

		/**
		 * 绑定onclick事件
		 */
		bindOnClick : function(func) {
			this.onClick = func;
		},
		/**
		 * 绑定双击事件
		 */
		bindOnDblClick : function(func) {
			this.ondblclick=func;
		},


		/**
		 * 加载节点
		 */
		loadNodes : function() {
			var zNodes = null;
			var ajax = new $ax(hdw.ctxPath + this.url, function(data) {
				zNodes = data;
			}, function(data) {
				hdw.error("加载ztree信息失败!");
			});
			ajax.start();
			return zNodes;
		},

		/**
		 * 获取选中的值
		 */
		getSelectedVal : function(){
			var zTree = $.fn.zTree.getZTreeObj(this.id);
			var nodes = zTree.getSelectedNodes();
			return nodes[0].name;
		},
		
		/**
		 * 获取选中的CheckBox的Id
		 */
		getCheckBoxId:function(){
			var zTree = $.fn.zTree.getZTreeObj(this.id);
			var nodes = zTree.getCheckedNodes(true);
			return nodes;
		},
        /**
		 * zTree 节点文字过多处理方法
         * @param treeId
         * @param treeNode
         */
        addDiyDomWithCheck:function (treeId, treeNode) {
        var spaceWidth = 5;
        var switchObj = $("#" + treeNode.tId + "_switch"),
            checkObj = $("#" + treeNode.tId + "_check"),
            icoObj = $("#" + treeNode.tId + "_ico");
        switchObj.remove();
        checkObj.remove();
        icoObj.parent().before(switchObj);
        icoObj.parent().before(checkObj);

        var spantxt = $("#" + treeNode.tId + "_span").html();
        $("#" + treeNode.tId + "_span").css({"fontSize":13});
        $("#" + treeNode.tId + "_span").attr("data-toggle","tooltip");
        $("#" + treeNode.tId + "_span").attr("data-placement","top");
        if (spantxt.length > 10) {
            spantxt = spantxt.substring(0, 10) + "...";
            $("#" + treeNode.tId + "_span").html(spantxt);
        }

    }
	};

	window.$ZTree = $ZTree;

}());