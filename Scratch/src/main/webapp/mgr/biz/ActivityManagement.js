/*
 * 活动管理
 * xs
 * 2018/04/19
 */
Ext.define('Business.ActivityManagement', {
	extend: 'Ext.ux.desktop.Module',

    requires: [
        'Ext.util.Format',
        'Ext.grid.Panel',
        'Ext.grid.RowNumberer'
    ],

    id: 'activity-mgr',
    
    title: '活动管理',

    init: function(){
        this.launcher = {
            text: '活动管理',
            iconCls:'activity'
        };
    },
    
    createNewWindow: function () {
        var me = this,
            desktop = me.app.getDesktop();

        me.store = Ext.create('Ext.data.Store',{
    		fields: ['id', 'appid', 'name', 'price', 'virNumber', 'actNumber', 'broNumber', 'imageUrl', 'enable', 'createdTime'],
    		proxy : {
    			type : 'ajax',
    			url : '/biz/activity/queryListByAppid.atc',
    			reader : {
    				root : 'actList'
    			}
    		}
    	});
        
        me.f = Ext.create('Ext.form.FormPanel',{
    		layout : 'anchor',
    		defaults : {
    			anchor : '100%',
    			layout : 'hbox',
    			xtype : 'fieldcontainer'
    		},
    		bodyPadding : '5 10 0 0',
    		border : false,
    		items : [ {
    			defaults : {flex : 1,xtype : 'textfield',labelWidth : 70,labelAlign : 'right'},
    			items : [ {
    				name : 'activity.name',
    				fieldLabel : '产品标题',
    			},{
    				name : 'activity.id',
    				xtype: 'hiddenfield'
    			}]
    		},{
    			defaults : {flex : 1,xtype : 'numberfield',labelWidth : 70,labelAlign : 'right'},
    			items : [{
    				name : 'activity.price',
    				fieldLabel : '价格'
    			},{
    				name: 'activity.virNumber',
    				fieldLabel : '虚拟参与数'
    			}]
    		},{
    			defaults : {flex : 1,xtype : 'numberfield',labelWidth : 70,labelAlign : 'right'},
    			items : [{
    				name : 'activity.broNumber',
    				fieldLabel : '浏览人数',
    				readOnly: true,
    				fieldStyle:'background-color: #F0F0F0;'
    			},{
    				name: 'activity.actNumber',
    				fieldLabel : '实际参与数',
    				readOnly: true,
    				fieldStyle:'background-color: #F0F0F0;'
    			}]
    		},{
    			defaults : {flex : 5,xtype : 'filefield',labelWidth : 70,labelAlign : 'right'},
    			items : [ {
    				fieldLabel : '大图',
    				name: 'image',
    				buttonText: '浏览...',
    				anchor : '99%'
    			},{
    				xtype : 'displayfield',
    				flex : 2,
    				labelWidth : 5,
    				fieldLabel : '&nbsp;',
    				labelSeparator: '',
    				value:'最佳尺寸540*518'
    			} ]
    		},{
    			defaults : {flex : 1,xtype : 'radiogroup',labelWidth : 70,labelAlign : 'right'},
    			items: [{
    				fieldLabel: '是否开启', 
    				defaults: {name: 'activity.enable'},
    	        	items: [{inputValue: true, boxLabel: '开启'}, {inputValue: false,boxLabel: '停止', checked: true}]
    			},{
    				xtype : 'displayfield',
    				fieldLabel : '&nbsp;',
    				labelSeparator: '',
    				value:''
    			}]
    		} ]
    	});
        
        me.tbar = [
            {text:'新增', iconCls:'add', handler: me.onAdd, scope: me },
            {text:'修改', iconCls:'pencil', handler: me.onEdit, scope: me },
            {text:'删除', iconCls:'delete', handler: me.onDelete, scope: me }
        ];
        
        me.buttons = [
            {text:'保 存', iconCls:'accept', handler: me.onSave, scope: me },
            {text:'关 闭', iconCls:'stop', handler: me.onCancel, scope: me }
        ];
        
        me.largeRenderer = function(value, metaData, record) {
    		if(value){
      			metaData.tdAttr = "data-qtip=\"<img src='/upload/activity/" + record.data.appid + '/' + value + "' style='width:475px; height:454px'/>\""; 
      			return '<img src="/upload/activity/' + record.data.appid + '/' + value + '" style="height:20px" onerror="this.src=\'/resources/img/noImage.png\'" />';
      		} else
      			return '<img src="/resources/img/noImage.png" style="height:20px" />';
        };
        
        me.selModel = Ext.create('Ext.selection.CheckboxModel', {
    		injectCheckbox : 1,
    		mode : 'SINGLE'
    	});
        
        me.subwin = Ext.create('Ext.Window', {
            width: 420,
            height: 230,
            constrain: true,
            layout: 'fit',
            closeAction: 'hide',
            items: [me.f],
            buttons: me.buttons
        });
    	
        return desktop.createWindow({
            id: me.id,
            title: me.title,
            width:800,
            height:480,
            iconCls: 'activity',
            animCollapse:false,
            constrainHeader:true,
            layout: 'fit',
            items: [{
            	border: false,
                xtype: 'grid',
                store: me.store,
                selModel: me.selModel,
                stripeRows : true,
                frame : false,
                forceFit: true,
                viewConfig : {enableTextSelection : true},
        		loadMask : {msg : '正在加载表格数据,请稍等...'},
        		columns: [new Ext.grid.RowNumberer({
                    	header : 'No',
                		width : '4%'
                    }), {
                		dataIndex : 'id',
                		hidden: true
                    },{
                    	text : '产品标题',
                    	dataIndex : 'name',
                		width : '20%'
                    },{
                    	text : '价格',
                    	dataIndex : 'price',
                    	width : '12%',
                    	renderer: Ext.util.Format.numberRenderer("0.00")
                    },{
                    	text : '浏览人数',
                		dataIndex : 'broNumber',
                		width : '10%'
                    },{
                    	text : '实际参与人数',
                    	dataIndex : 'actNumber',
                    	width : '10%'
                    },{
                    	text : '虚拟参与人数',
                    	dataIndex : 'virNumber',
                    	width : '10%'
                    },{
                    	text : '图片',
                    	dataIndex : 'imageUrl',
                    	width : '8%',
                    	renderer: me.largeRenderer
                    },{
                    	text : '是否可用',
                    	dataIndex : 'enable',
                    	width : '10%',
                    	renderer: function(value){
            				return value ? '<font color=green>正常</font>' : '<font color=red>锁定</font>';
            			}
                    },{
                    	text : '创建时间',
                    	dataIndex : 'createdTime',
                    	width : '17%'
                    }]
                }, me.subwin
            ],
            tbar: me.tbar,
            listeners: {
                show: function() {
                	me.store.load();
                }
            }
        });
    },
    
    createWindow: function(){
    	var win = this.app.getDesktop().getWindow(this.id);
        if (!win) {
            win = this.createNewWindow();
        }
        return win;
    },
    
    onAdd: function() {
    	var me = this;
    	me.subwin.setTitle('新建活动');
    	me.f.getForm().reset();
    	me.f.getForm().url = '/biz/activity/insertActivity.atc';
    	me.subwin.show();
    },
    
    onEdit: function() {
    	var me = this;
    	var record = me.selModel.getSelection()[0];
		if (Ext.isEmpty(record)) {
			Ext.MessageBox.show({
				title : '提示',
				msg : '你没有选中任何项目！',
				buttons : Ext.MessageBox.OK,
				icon : Ext.MessageBox.INFO
			});
			return;
		}
		me.subwin.setTitle('修改活动');
    	me.f.getForm().reset();
    	me.f.getForm().url = '/biz/activity/updateActivity.atc';
    	me.selModel.deselectAll();
    	me.f.getForm().setValues([
    		{id:'activity.id', value:record.data.id},
			{id:'activity.name', value:record.data.name},
			{id:'activity.price', value:record.data.price},
			{id:'activity.virNumber', value:record.data.virNumber},
			{id:'activity.actNumber', value:record.data.actNumber},
			{id:'activity.broNumber', value:record.data.broNumber},
			{id:'activity.enable', value:record.data.enable}
		]);
		//Ext.getCmp('enable').setValue({'activity.enable' : record.data.enable});
    	me.subwin.show();
    },
    
    onDelete: function() {
    	var me = this, desktop = me.app.getDesktop();
    	var record = me.selModel.getSelection()[0];
		if (Ext.isEmpty(record)) {
			Ext.MessageBox.show({
				title : '提示',
				msg : '你没有选中任何项目！',
				buttons : Ext.MessageBox.OK,
				icon : Ext.MessageBox.INFO
			});
			return;
		}
		Ext.Msg.confirm('请确认', '确定要删除这项吗?', function(btn, text) {
			if (btn == 'yes') {
				Ext.Ajax.request({
					url : '/biz/activity/deleteActivity.atc',
					params : {
						'activity.id' : record.data.id
					},
					success : function(resp, opts) {
						var result = Ext.decode(resp.responseText);
						if (result.success) {
							desktop.showMessage(result.msg);
							me.selModel.deselectAll();
							me.store.reload();
						} else
							Ext.MessageBox.show({
								title : '提示',
								msg : result.msg,
								buttons : Ext.MessageBox.OK,
								icon : Ext.MessageBox.ERROR
							});
					},
					failure : function(resp, opts) {
						var result = Ext.decode(resp.responseText);
						Ext.MessageBox.show({
							title : '提示',
							msg : result.msg,
							buttons : Ext.MessageBox.OK,
							icon : Ext.MessageBox.ERROR
						});
					}
				});
			}
		});
    },
    
    onSave: function() {
    	var me = this, desktop = me.app.getDesktop();
    	if (me.f.form.isValid()) {
			me.f.form.submit({
				waitTitle : '提示',
				method : 'POST',
				waitMsg : '正在处理数据,请稍候...',
				success : function(form, action) {
					me.subwin.hide();
					desktop.showMessage(action.result.msg);
					me.store.reload();
				},
				failure : function(form, action) {
					var msg = action.result.msg;
					Ext.MessageBox.alert('提示', msg);
				}
			});
		}
    },
    
    onCancel: function() {
    	var me = this;
    	me.subwin.hide();
    }

});

