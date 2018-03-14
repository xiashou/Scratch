/*

    Excel.js - convert an Ext 4 grid into an Excel spreadsheet using nothing but
    javascript and good intentions.

    By: Steve Drucker
    October 26, 2013
    Original Ext 3 Implementation by: Nige "Animal" White?
    
    Updated: March 19, 2014 to support grouped grids/stores
    Updated: April 3, 2014 to support Internet Explorer
    
    Contact Info:

    e. sdrucker@figleaf.com
    blog: druckit.wordpress.com
    linkedin: www.linkedin.com/in/uberfig
    git: http://github.com/sdruckerfig
    company: Fig Leaf Software (http://www.figleaf.com / http://training.figleaf.com)

    Invocation:  grid.downloadExcelXml(includeHiddenColumns,title)

*/


var Base64 = (function() {
    // Private property
    var keyStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";

    // Private method for UTF-8 encoding

    function utf8Encode(string) {
        string = string.replace(/\r\n/g, "\n");
        var utftext = "";
        for (var n = 0; n < string.length; n++) {
            var c = string.charCodeAt(n);
            if (c < 128) {
                utftext += String.fromCharCode(c);
            } else if ((c > 127) && (c < 2048)) {
                utftext += String.fromCharCode((c >> 6) | 192);
                utftext += String.fromCharCode((c & 63) | 128);
            } else {
                utftext += String.fromCharCode((c >> 12) | 224);
                utftext += String.fromCharCode(((c >> 6) & 63) | 128);
                utftext += String.fromCharCode((c & 63) | 128);
            }
        }
        return utftext;
    }

    // Public method for encoding
    return {
        encode: (typeof btoa == 'function') ? function(input) {
            return btoa(utf8Encode(input));
        } : function(input) {
            var output = "";
            var chr1, chr2, chr3, enc1, enc2, enc3, enc4;
            var i = 0;
            input = utf8Encode(input);
            while (i < input.length) {
                chr1 = input.charCodeAt(i++);
                chr2 = input.charCodeAt(i++);
                chr3 = input.charCodeAt(i++);
                enc1 = chr1 >> 2;
                enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);
                enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);
                enc4 = chr3 & 63;
                if (isNaN(chr2)) {
                    enc3 = enc4 = 64;
                } else if (isNaN(chr3)) {
                    enc4 = 64;
                }
                output = output +
                    keyStr.charAt(enc1) + keyStr.charAt(enc2) +
                    keyStr.charAt(enc3) + keyStr.charAt(enc4);
            }
            return output;
        }
    };
})();

Ext.define('MyApp.view.override.Grid', {
    override: 'Ext.grid.GridPanel',
    requires: 'Ext.form.action.StandardSubmit',

    /*
        Kick off process
    */

    downloadExcelXml: function(includeHidden, title) {

        if (!title) title = this.title;

        var vExportContent = this.getExcelXml(includeHidden, title);

        var location = 'data:application/vnd.ms-excel;base64,' + Base64.encode(vExportContent);

        /* 
          dynamically create and anchor tag to force download with suggested filename 
          note: download attribute is Google Chrome specific
        */
		
        if (Ext.isChrome) {
            var gridEl = this.getEl();

            var el = Ext.DomHelper.append(gridEl, {
                tag: "a",
                download: title + "-" + Ext.Date.format(new Date(), 'Y-m-d Hi') + '.xls',
                href: location
            });

            el.click();

            Ext.fly(el).destroy();

        } else {

            var form = this.down('form#uploadForm');
            if (form) {
                form.destroy();
            }
            form = this.add({
                xtype: 'form',
                itemId: 'uploadForm',
                hidden: true,
                standardSubmit: true,
                url: 'http://webapps.figleaf.com/dataservices/Excel.cfc?method=echo&mimetype=application/vnd.ms-excel&filename=' + escape(title + ".xls"),
                items: [{
                    xtype: 'hiddenfield',
                    name: 'data',
                    value: vExportContent
                }]
            });

            form.getForm().submit();

        }
    },

    /*

        Welcome to XML Hell
        See: http://msdn.microsoft.com/en-us/library/office/aa140066(v=office.10).aspx
        for more details

    */
    getExcelXml: function(includeHidden, title) {

        var theTitle = title || this.title;

        var worksheet = this.createWorksheet(includeHidden, theTitle);
        var totalWidth = this.columnManager.columns.length;

        return ''.concat(
            '<?xml version="1.0"?>',
            '<Workbook xmlns="urn:schemas-microsoft-com:office:spreadsheet" xmlns:o="urn:schemas-microsoft-com:office:office" xmlns:x="urn:schemas-microsoft-com:office:excel" xmlns:ss="urn:schemas-microsoft-com:office:spreadsheet" xmlns:html="http://www.w3.org/TR/REC-html40">',
            '<DocumentProperties xmlns="urn:schemas-microsoft-com:office:office"><Title>' + theTitle + '</Title></DocumentProperties>',
            '<OfficeDocumentSettings xmlns="urn:schemas-microsoft-com:office:office"><AllowPNG/></OfficeDocumentSettings>',
            '<ExcelWorkbook xmlns="urn:schemas-microsoft-com:office:excel">',
            '<WindowHeight>' + worksheet.height + '</WindowHeight>',
            '<WindowWidth>' + worksheet.width + '</WindowWidth>',
            '<ProtectStructure>False</ProtectStructure>',
            '<ProtectWindows>False</ProtectWindows>',
            '</ExcelWorkbook>',

            '<Styles>',

            '<Style ss:ID="Default" ss:Name="Normal">',
            '<Alignment ss:Vertical="Bottom"/>',
            '<ss:Borders>',
            '<ss:Border ss:Color="#e4e4e4" ss:Weight="1" ss:LineStyle="Continuous" ss:Position="Top" />',
            '<ss:Border ss:Color="#e4e4e4" ss:Weight="1" ss:LineStyle="Continuous" ss:Position="Bottom" />',
            '<ss:Border ss:Color="#e4e4e4" ss:Weight="1" ss:LineStyle="Continuous" ss:Position="Left" />',
            '<ss:Border ss:Color="#e4e4e4" ss:Weight="1" ss:LineStyle="Continuous" ss:Position="Right" />',
            '</ss:Borders>',
            '<Font ss:Size="12" />',
            '<Interior/>',
            '<NumberFormat/>',
            '<Protection/>',
            '</Style>',

            '<Style ss:ID="title">',
            '<ss:Borders>',
            '<ss:Border ss:Color="#e4e4e4" ss:Weight="1" ss:LineStyle="Continuous" ss:Position="Top" />',
            '<ss:Border ss:Color="#e4e4e4" ss:Weight="1" ss:LineStyle="Continuous" ss:Position="Bottom" />',
            '<ss:Border ss:Color="#e4e4e4" ss:Weight="1" ss:LineStyle="Continuous" ss:Position="Left" />',
            '<ss:Border ss:Color="#e4e4e4" ss:Weight="1" ss:LineStyle="Continuous" ss:Position="Right" />',
            '</ss:Borders>',
            '<Font ss:Bold="1" ss:Size="18" />',
            '<Alignment ss:Horizontal="Center" ss:Vertical="Center" ss:WrapText="1" />',
            '<NumberFormat ss:Format="@" />',
            '</Style>',

            '<Style ss:ID="headercell">',
            '<Font ss:Bold="1" ss:Size="10" />',
            '<Alignment ss:Horizontal="Center" ss:WrapText="1" />',
            '<Interior ss:Pattern="Solid" />',
            '</Style>',


            '<Style ss:ID="even">',
            '<Interior ss:Pattern="Solid" />',
            '</Style>',


            '<Style ss:ID="evendate" ss:Parent="even">',
            '<NumberFormat ss:Format="yyyy-mm-dd" />',
            '</Style>',


            '<Style ss:ID="evenint" ss:Parent="even">',
            '<Numberformat ss:Format="0" />',
            '</Style>',

            '<Style ss:ID="evenfloat" ss:Parent="even">',
            '<Numberformat ss:Format="0.00" />',
            '</Style>',

            '<Style ss:ID="odd">',
            '<Interior ss:Pattern="Solid" />',
            '</Style>',

            '<Style ss:ID="groupSeparator">',
            '<Interior ss:Pattern="Solid" />',
            '</Style>',

            '<Style ss:ID="odddate" ss:Parent="odd">',
            '<NumberFormat ss:Format="yyyy-mm-dd" />',
            '</Style>',

            '<Style ss:ID="oddint" ss:Parent="odd">',
            '<NumberFormat Format="0" />',
            '</Style>',

            '<Style ss:ID="oddfloat" ss:Parent="odd">',
            '<NumberFormat Format="0.00" />',
            '</Style>',


            '</Styles>',
            worksheet.xml,
            '</Workbook>'
        );
    },

    /*

        Support function to return field info from store based on fieldname

    */

    getModelField: function(fieldName) {

        var fields = this.store.model.getFields();
        for (var i = 0; i < fields.length; i++) {
            if (fields[i].name === fieldName) {
                return fields[i];
            }
        }
    },

    /*
        
        Convert store into Excel Worksheet

    */
    generateEmptyGroupRow: function(dataIndex, value, cellTypes, includeHidden) {


        var cm = this.columnManager.columns;
        var colCount = cm.length;
        var rowTpl = '<Row ss:AutoFitHeight="0"><Cell ss:StyleID="groupSeparator" ss:MergeAcross="{0}"><Data ss:Type="String"><html:b>{1}</html:b></Data></Cell></Row>';
        var visibleCols = 0;

        // rowXml += '<Cell ss:StyleID="groupSeparator">'

        for (var j = 0; j < colCount; j++) {
            if (cm[j].xtype != 'actioncolumn' && (cm[j].dataIndex != '') && (includeHidden || !cm[j].hidden)) {
                // rowXml += '<Cell ss:StyleID="groupSeparator"/>';
                visibleCols++;
            }
        }

        // rowXml += "</Row>";

        return Ext.String.format(rowTpl, visibleCols - 1, value);
    },


    createWorksheet: function(includeHidden, theTitle) {
        // Calculate cell data types and extra class names which affect formatting
        var cellType = [];
        var cellTypeClass = [];
        var cm = this.columnManager.columns;
        var headcm = this.columnManager.headerCt.items.items;

        var totalWidthInPixels = 0;
        var colXml = '';
        var headerXml = '';
        var visibleColumnCountReduction = 0;
        var colCount = cm.length;
        var j = 0;
        var isVar = true;
        var k = 1;
        for (var i = 0; i < colCount; i++) {
            if (cm[i].xtype != 'actioncolumn' && (cm[i].dataIndex != '') && (includeHidden || !cm[i].hidden)) {
                var w = cm[i].getEl().getWidth();
                totalWidthInPixels += w;

                if (cm[i].text === "") {
                    cellType.push("None");
                    cellTypeClass.push("");
                    ++visibleColumnCountReduction;
                } else {
                    colXml += '<Column ss:AutoFitWidth="1" ss:Width="' + w + '" />';
                    
                    //////处理双表头逻辑
                    if(headcm[i] && headcm[i].items.length > 0){
                    	if(isVar){
                    		j = i;
                    		k = j % 2;
                    		isVar = false;
                    	}
                    }
                    
                    if(j>0){
                    	headerXml += '<Cell ss:StyleID="headercell">' +
	                        '<Data ss:Type="String">' + headcm[j].text + '/' + cm[i].text + '</Data>' +
	                        '<NamedCell ss:Name="Print_Titles"></NamedCell></Cell>';
                    	if(i%2 == k-1)
                    		j++;
                    } else {
                    	headerXml += '<Cell ss:StyleID="headercell">' +
	                        '<Data ss:Type="String">' + cm[i].text + '</Data>' +
	                        '<NamedCell ss:Name="Print_Titles"></NamedCell></Cell>';
                    }
                    //////处理双表头逻辑


                    var fld = this.getModelField(cm[i].dataIndex);
                    if(typeof(fld) != 'undefined')
	                    switch (fld.type.type) {
	                        case "int":
	                            cellType.push("Number");
	                            cellTypeClass.push("int");
	                            break;
	                        case "float":
	                            cellType.push("Number");
	                            cellTypeClass.push("float");
	                            break;
	
	                        case "bool":
	
	                        case "boolean":
	                            cellType.push("String");
	                            cellTypeClass.push("");
	                            break;
	                        case "date":
	                            cellType.push("DateTime");
	                            cellTypeClass.push("date");
	                            break;
	                        default:
	                            cellType.push("String");
	                            cellTypeClass.push("");
	                            break;
	                    }
                    else {
                    	cellType.push("String");
                        cellTypeClass.push("");
                    }
                }
            }
        }
        var visibleColumnCount = cellType.length - visibleColumnCountReduction;

        var result = {
            height: 9000,
            width: Math.floor(totalWidthInPixels * 30) + 50
        };

        // Generate worksheet header details.

        // determine number of rows
        var numGridRows = this.store.getCount() + 2;
        if (!Ext.isEmpty(this.store.groupField) || this.store.groupers.items.length > 0) {
            numGridRows = numGridRows + this.store.getGroups().length;
        }

        // create header for worksheet
        var t = ''.concat(
            '<Worksheet ss:Name="' + theTitle + '">',

            '<Names>',
            '<NamedRange ss:Name="Print_Titles" ss:RefersTo="=\'' + theTitle + '\'!R1:R2">',
            '</NamedRange></Names>',

            '<Table ss:ExpandedColumnCount="' + (visibleColumnCount + 2),
            '" ss:ExpandedRowCount="' + numGridRows + '" x:FullColumns="1" x:FullRows="1" ss:DefaultColumnWidth="65" ss:DefaultRowHeight="15">',
            colXml,
            '<Row ss:Height="38">',
            '<Cell ss:MergeAcross="' + (visibleColumnCount - 1) + '" ss:StyleID="title">',
            '<Data ss:Type="String" xmlns:html="http://www.w3.org/TR/REC-html40">',
            '<html:b>' + theTitle + '</html:b></Data><NamedCell ss:Name="Print_Titles">',
            '</NamedCell></Cell>',
            '</Row>',
            '<Row ss:AutoFitHeight="1">',
            headerXml +
            '</Row>'
        );

        // Generate the data rows from the data in the Store
        var groupVal = "";
        var groupField = "";
        if (this.store.groupers.keys.length > 0) {
            groupField = this.store.groupers.keys[0];
        }
        for (var i = 0, it = this.store.data.items, l = it.length; i < l; i++) {

            if (!Ext.isEmpty(groupField)) {
                if (groupVal != this.store.getAt(i).get(groupField)) {
                    groupVal = this.store.getAt(i).get(groupField);
                    t += this.generateEmptyGroupRow(groupField, groupVal, cellType, includeHidden);
                }
            }
            t += '<Row>';
            var cellClass = (i & 1) ? 'odd' : 'even';
            r = it[i].data;
            var k = 0;
            for (var j = 0; j < colCount; j++) {
                if (cm[j].xtype != 'actioncolumn' && (cm[j].dataIndex != '') && (includeHidden || !cm[j].hidden)) {
                    var v = r[cm[j].dataIndex];
                    v = v == null ? "" : v;
                    if (cellType[k] !== "None") {
                        t += '<Cell ss:StyleID="' + cellClass + cellTypeClass[k] + '"><Data ss:Type="' + cellType[k] + '">';
                        if (cellType[k] == 'DateTime') {
                            t += Ext.Date.format(v, 'Y-m-d');
                        } else {
                        	if(typeof(v) == 'object'){
                        		var number = '';
                        		if(typeof(v[0]) != 'undefined' && v[0] != 'null' && v[0] != null){
                        			if(typeof(v[0].carShort) != 'undefined' && v[0].carShort != 'null' && v[0].carShort != null)
                        				number += v[0].carShort;
                        			if(typeof(v[0].carCode) != 'undefined' && v[0].carCode != 'null' && v[0].carCode != null)
                        				number += v[0].carCode;
                        			if(typeof(v[0].carNumber) != 'undefined' && v[0].carNumber != 'null' && v[0].carNumber != null)
                        				number += v[0].carNumber;
                        			v = number != '' ? number : v;
                        		}
                        	}
                        	
                            t += (typeof(v) != 'undefined' ? v : "");
                        }
                        t += '</Data></Cell>';
                    }
                    k++;
                }
            }
            t += '</Row>';
        }

        result.xml = t.concat(
            '</Table>',
            '<WorksheetOptions xmlns="urn:schemas-microsoft-com:office:excel">',
            '<PageLayoutZoom>0</PageLayoutZoom>',
            '<Selected/>',
            '<Panes>',
            '<Pane>',
            '<Number>3</Number>',
            '<ActiveRow>2</ActiveRow>',
            '</Pane>',
            '</Panes>',
            '<ProtectObjects>False</ProtectObjects>',
            '<ProtectScenarios>False</ProtectScenarios>',
            '</WorksheetOptions>',
            '</Worksheet>'
        );
        return result;
    }
});

// or replace all text with a URL