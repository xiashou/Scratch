/*!
 * Ext JS Library 4.0
 * Copyright(c) 2006-2011 Sencha Inc.
 * licensing@sencha.com
 * http://www.sencha.com/license
 */

Ext.define('MyDesktop.DeptReport', {
	extend: 'Ext.ux.desktop.Module',

    requires: [
        'Ext.util.Format',
        'Ext.grid.Panel',
        'Ext.grid.RowNumberer'
    ],

    id:'dept-rept',

    init : function(){
        this.launcher = {
            text: '门店列表',
            iconCls:'store'
        };
    },

    createWindow : function(){
        var desktop = this.app.getDesktop();
        var win = desktop.getWindow('dept-rept');
        var store = new Ext.data.Store({
    		pageSize : 500,
    		fields: ['id', 'parentId', 'deptName', 'deptCode', 'deptType', 'areaId', 'createTime', 'enable'],
    		proxy : {
    			type : 'ajax',
    			url : '/sys/querySysDeptPage.atc',
    			reader : {
    				root : 'deptList'
    			}
    		}
    	});
        var pagesizeCombo = new Ext.form.ComboBox({
    		name : 'pagesize',
    		triggerAction : 'all',
    		mode : 'local',
    		store : new Ext.data.ArrayStore({
    			fields : [ 'value', 'text' ],
    			data : [ [ 10, '10条/页' ], [ 20, '20条/页' ], [ 50, '50条/页' ],
    					[ 100, '100条/页' ], [ 200, '200条/页' ], [ 500, '500条/页' ],
    					[ 1000000, '更多条' ] ]
    		}),
    		valueField : 'value',
    		displayField : 'text',
    		value : 500,
    		editable : false,
    		width : 85
    	});

    	var number = parseInt(pagesizeCombo.getValue());

    	// 改变每页显示条数reload数据
    	pagesizeCombo.on("select", function(comboBox) {
    		bbar.pageSize = parseInt(comboBox.getValue());
    		number = parseInt(comboBox.getValue());
    		store.pageSize = parseInt(comboBox.getValue());
    		store.reload({
    			params : {
    				start : 0,
    				limit : bbar.pageSize
    			}
    		});
    	});
    	
    	var bbar = new Ext.PagingToolbar({
    		pageSize : number,
    		store : store,
    		displayInfo : true,
    		displayMsg : '显示{0}条到{1}条,共{2}条',
    		emptyMsg : "没有符合条件的记录",
    		items : [ '-', '&nbsp;&nbsp;', pagesizeCombo ]
    	});
        if(!win){
            win = desktop.createWindow({
                id: 'dept-rept',
                title:'门店列表',
                width:800,
                height:520,
                iconCls: 'store',
                animCollapse:false,
                constrainHeader:true,
                layout: 'fit',
                items: [
                    {
                        border: false,
                        xtype: 'grid',
                        store: store,
                        columns: [
                            new Ext.grid.RowNumberer(),
                            {
                            	text : '名称',
                        		width : 280,
                        		dataIndex : 'deptName',
                        		editor: {xtype: 'textfield'},
                              	items: {
                              		xtype: 'textfield',
                              		flex : 1,
                              		margin: 2,
                              		enableKeyEvents: true,
                              		listeners: {
                              			keyup: function() {
                              				store.clearFilter();
                              				if (this.value) {
                              					store.filter({
                              						property     : 'deptName',
                              						value         : this.value,
                              						anyMatch      : true,
                              						caseSensitive : false
                              					});
                              				}
                              			},
                              			buffer: 500
                              		}
                              	}
                            },
                            {
                            	text : '编码',
                        		dataIndex : 'id',
                        		width : 180
                            },
                            {
                            	text : '业务对照码',
                        		dataIndex : 'deptCode',
                        		width : 100,
                        		editor: {xtype: 'textfield'},
                              	items: {
                              		xtype: 'textfield',
                              		flex : 1,
                              		margin: 2,
                              		enableKeyEvents: true,
                              		listeners: {
                              			keyup: function() {
                              				store.clearFilter();
                              				if (this.value) {
                              					store.filter({
                              						property     : 'deptCode',
                              						value         : this.value,
                              						anyMatch      : true,
                              						caseSensitive : false
                              					});
                              				}
                              			},
                              			buffer: 500
                              		}
                              	}
                            },
                            {
                            	text : '创建时间',
                        		dataIndex : 'createTime',
                        		width : 180
                            }
                        ]
                    }
                ],
//                tbar:[{
//                    text:'Add Something',
//                    tooltip:'Add a new row',
//                    iconCls:'add'
//                }, '-', {
//                    text:'Options',
//                    tooltip:'Modify options',
//                    iconCls:'option'
//                },'-',{
//                    text:'Remove Something',
//                    tooltip:'Remove the selected item',
//                    iconCls:'remove'
//                }],
                bbar:bbar,
                listeners: {
                    show: function() {
                    	store.load({
                    		params:{
                    			'dept.deptType':3
                    		}
                    	});
                    }
                }
            });
        }
        return win;
    }

//    statics: {
//        getDummyData: function () {
//            return [
//                ['3m Co',71.72,0.02,0.03],
//                ['Alcoa Inc',29.01,0.42,1.47],
//                ['American Express Company',52.55,0.01,0.02],
//                ['American International Group, Inc.',64.13,0.31,0.49],
//                ['AT&T Inc.',31.61,-0.48,-1.54],
//                ['Caterpillar Inc.',67.27,0.92,1.39],
//                ['Citigroup, Inc.',49.37,0.02,0.04],
//                ['Exxon Mobil Corp',68.1,-0.43,-0.64],
//                ['General Electric Company',34.14,-0.08,-0.23],
//                ['General Motors Corporation',30.27,1.09,3.74],
//                ['Hewlett-Packard Co.',36.53,-0.03,-0.08],
//                ['Honeywell Intl Inc',38.77,0.05,0.13],
//                ['Intel Corporation',19.88,0.31,1.58],
//                ['Johnson & Johnson',64.72,0.06,0.09],
//                ['Merck & Co., Inc.',40.96,0.41,1.01],
//                ['Microsoft Corporation',25.84,0.14,0.54],
//                ['The Coca-Cola Company',45.07,0.26,0.58],
//                ['The Procter & Gamble Company',61.91,0.01,0.02],
//                ['Wal-Mart Stores, Inc.',45.45,0.73,1.63],
//                ['Walt Disney Company (The) (Holding Company)',29.89,0.24,0.81]
//            ];
//        }
//    }
});

