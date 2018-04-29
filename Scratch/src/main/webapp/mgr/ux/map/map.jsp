<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>腾讯地图开放API - 轻快小巧,简单易用!</title>
<link rel="stylesheet" href="/mgr/ux/map/css/common.css">
<script src="/mgr/ux/map/js/jquery-1.9.1.min.js" type="text/javascript"></script>
<link rel="stylesheet" href="/mgr/ux/map/css/jquery-ui.min.css">
<script src="/mgr/ux/map/js/jquery-ui-1.10.4.min.js"></script>
<script charset="utf-8" src="http://map.qq.com/api/js?v=2.exp"></script>
<style type="text/css">
* {
    margin: 0px;
    padding: 0px;
}

#main {
    height: 520px;
    border-top: 0px;
}

#bside_left {
    width: 260px;
    height: 510px;
    padding: 10px 0px 10px 10px;
    float: left;
    overflow: auto;
}

#level {
    margin-left: 20px;
}


.logo_img {
    width: 172px;
    height: 23px;
}

.poi {
    width: 620px;
    padding-top: 5px;
    padding-left: 5px;
    float: left;
    position: relative;
}

.poi_note {
    width: 63px;
    line-height: 26px;
    float: left;
}

#poi_cur {
    width: 160px;
    height: 22px;
    margin-right: 10px;
    margin-top: 3px;
    line-height: 26px;

    float: left;
    text-align: center;
}

#addr_cur {
    width: 260px;
    height: 22px;
    margin-right: 5px;
    margin-top: 3px;
    line-height: 26px;

    float: left;
}

.btn_copy {
    width: 49px;
    height: 24px;
    background: url(../img/btn_cpoy.png) 0px 0px;
    float: left;
}

.already {
    width: 52px;
    line-height: 26px;
    padding-left: 5px;
    float: left;
    color: red;
    display: none;
}

.info_list {
    margin-bottom: 5px;
    cleat: both;
    cursor: pointer;
}

#txt_pannel {
    height: 500px;
}

.hide {
    display: none;
}

#bside_rgiht {
    float: left;
    font-size: 12px;
}

#container {
    width: 620px;
    height: 520px;
}

#no_value{
    color:red;
    position: relative;
    width:200px;
}
.accept{
	line-height: 21px;
    margin-top: 3px;
    width: 40px;
}
</style>

</head>


<body>
<div style="position:relative;">
<div style="height:43px;">
    <div class="poi">
        <div class="poi_note">当前坐标：</div>
        <input type="text" id="poi_cur" />
        <div class="poi_note">当前地址：</div>
        <input type="text" id="addr_cur" />
    </div>
</div>
<div id="main">
    <div id="bside_rgiht">
        <div id="container"></div>
    </div>
</div>
</div>

<script type="text/javascript">

var container = document.getElementById("container");
var map = new qq.maps.Map(container, {
            zoom: 10

        }),
    label = new qq.maps.Label({
         map: map,
         offset: new qq.maps.Size(15,-12),
         draggable: false,
         clickable: false
    }),
    markerArray = [],
    curCity = document.getElementById("cur_city"),
    btnSearch = document.getElementById("btn_search"),
    bside = document.getElementById("bside_left"),
    url, query_city,
    cityservice = new qq.maps.CityService({
        complete: function (result) {
            //curCity.children[0].innerHTML = result.detail.name;
            map.setCenter(result.detail.latLng);
        }
    });
cityservice.searchLocalCity();
map.setOptions({
    draggableCursor: "crosshair"
});

$(container).mouseenter(function () {
    label.setMap(map);
});
$(container).mouseleave(function () {
    label.setMap(null);
});

qq.maps.event.addListener(map, "mousemove", function (e) {
    var latlng = e.latLng;
    label.setPosition(latlng);
    label.setContent(latlng.getLat().toFixed(6) + "," + latlng.getLng().toFixed(6));
});

var url3;
qq.maps.event.addListener(map, "click", function (e) {
	//console.log(parent.document.getElementById("locationx"));
	parent.document.getElementsByName("store.locationx")[0].value = e.latLng.getLat().toFixed(6);
	parent.document.getElementsByName("store.locationy")[0].value = e.latLng.getLng().toFixed(6);
    document.getElementById("poi_cur").value = e.latLng.getLat().toFixed(6) + "," + e.latLng.getLng().toFixed(6);
    url3 = encodeURI("http://apis.map.qq.com/ws/geocoder/v1/?location=" + e.latLng.getLat() + "," + e.latLng.getLng() + "&key=PQHBZ-3N4KJ-3RQFU-KCDXS-SGUE7-ECFYS&output=jsonp&&callback=?");
    $.getJSON(url3, function (result) {
        if(result.result!=undefined){
        	parent.document.getElementsByName("store.address")[0].value = result.result.address;
            document.getElementById("addr_cur").value = result.result.address;
        }else{
        	parent.document.getElementById("address-inputEl").value = "";
            document.getElementById("addr_cur").value = "";
        }

    })
});
/*
qq.maps.event.addListener(map, "zoom_changed", function () {
    document.getElementById("level").innerHTML = "当前缩放等级：" + map.getZoom();
});
*/
var listener_arr = [];
var isNoValue = false;
/*
qq.maps.event.addDomListener(btnSearch, 'click', function () {
    var value = this.parentNode.getElementsByTagName("input")[0].value;
    var latlngBounds = new qq.maps.LatLngBounds();
    for(var i= 0,l=listener_arr.length;i<l;i++){
        qq.maps.event.removeListener(listener_arr[i]);
    }
    listener_arr.length = 0;
    query_city = curCity.children[0].innerHTML;
    url = encodeURI("http://apis.map.qq.com/ws/place/v1/search?keyword=" + value + "&boundary=region(" + query_city + ",0)&page_size=9&page_index=1&key=PQHBZ-3N4KJ-3RQFU-KCDXS-SGUE7-ECFYS&output=jsonp&&callback=?");
    $.getJSON(url, function (result) {

        if (result.count) {
            isNoValue = false;
            bside.innerHTML = "";
            each(markerArray, function (n, ele) {
                ele.setMap(null);
            });
            markerArray.length = 0;
            each(result.data, function (n, ele) {
                var latlng = new qq.maps.LatLng(ele.location.lat, ele.location.lng);
                latlngBounds.extend(latlng);
                var left = n * 27;
                var marker = new qq.maps.Marker({
                    map: map,
                    position: latlng,
                    zIndex: 10
                });
                marker.index = n;
                marker.isClicked = false;
                setAnchor(marker, true);
                markerArray.push(marker);
                var listener1 = qq.maps.event.addDomListener(marker, "mouseover", function () {
                    var n = this.index;
                    setCurrent(markerArray, n, false);
                    setCurrent(markerArray, n, true);
                    label.setContent(this.position.getLat().toFixed(6) + "," + this.position.getLng().toFixed(6));
                    label.setPosition(this.position);
                    label.setOptions({
                        offset: new qq.maps.Size(15, -20)
                    })

                });
                listener_arr.push(listener1);
                var listener2 = qq.maps.event.addDomListener(marker, "mouseout", function () {
                    var n = this.index;
                    setCurrent(markerArray, n, false);
                    setCurrent(markerArray, n, true);
                    label.setOptions({
                        offset: new qq.maps.Size(15, -12)
                    })
                });
                listener_arr.push(listener2);
                var listener3 = qq.maps.event.addDomListener(marker, "click", function () {
                    var n = this.index;
                    setFlagClicked(markerArray, n);
                    setCurrent(markerArray, n, false);
                    setCurrent(markerArray, n, true);
                    document.getElementById("addr_cur").value = bside.childNodes[n].childNodes[1].childNodes[1].innerHTML.substring(3);
                });
                listener_arr.push(listener3);
                map.fitBounds(latlngBounds);
                var div = document.createElement("div");
                div.className = "info_list";
                var order = document.createElement("div");
                var leftn = -54 - 17 * n;
                order.style.cssText = "width:17px;height:17px;margin:3px 3px 0px 0px;float:left;background:url(./img/marker_n.png) " + leftn + "px 0px";
                div.appendChild(order);
                var pannel = document.createElement("div");
                pannel.style.cssText = "width:200px;float:left;";
                div.appendChild(pannel);
                var name = document.createElement("p");
                name.style.cssText = "margin:0px;color:#0000CC";
                name.innerHTML = ele.title;
                pannel.appendChild(name);
                var address = document.createElement("p");
                address.style.cssText = "margin:0px;";
                address.innerHTML = "地址：" + ele.address;
                pannel.appendChild(address);
                if (ele.tel != undefined) {
                    var phone = document.createElement("p");
                    phone.style.cssText = "margin:0px;";
                    phone.innerHTML = "电话：" + ele.tel;
                    pannel.appendChild(phone);
                }
                var position = document.createElement("p");
                position.style.cssText = "margin:0px;";
                position.innerHTML = "坐标：" + ele.location.lat.toFixed(6) + "，" + ele.location.lng.toFixed(6);
                pannel.appendChild(position);
                bside.appendChild(div);
                console.log("pannel.offsetHeight",pannel.offsetHeight)
                div.style.height = pannel.offsetHeight + "px";
                div.isClicked = false;
                div.index = n;
                marker.div = div;
                div.marker = marker;
            });
            $("#bside_left").delegate(".info_list", "mouseover", function (e) {

                var n = this.index;

                setCurrent(markerArray, n, false);
                setCurrent(markerArray, n, true);
            });
            $("#bside_left").delegate(".info_list", "mouseout", function () {
                each(markerArray, function (n, ele) {
                    if (!ele.isClicked) {
                        setAnchor(ele, true);
                        ele.div.style.background = "#fff";
                    }
                })
            });
            $("#bside_left").delegate(".info_list", "click", function (e) {
                var n = this.index;
                setFlagClicked(markerArray, n);
                setCurrent(markerArray, n, false);
                setCurrent(markerArray, n, true);
                map.setCenter(markerArray[n].position);
                document.getElementById("addr_cur").value = this.childNodes[1].childNodes[1].innerHTML.substring(3);
            });
        } else {

            bside.innerHTML = "";
            each(markerArray, function (n, ele) {
                ele.setMap(null);
            });
            markerArray.length = 0;
            var novalue = document.createElement('div');
            novalue.id = "no_value";
            novalue.innerHTML = "对不起，没有搜索到您要找的结果!";
            bside.appendChild(novalue);
            isNoValue = true;
        }
    });
});

btnSearch.onmousedown = function () {
    this.className = "btn_active";
};
btnSearch.onmouseup = function () {
    this.className = "btn";
};
*/
function setAnchor(marker, flag) {
    var left = marker.index * 27;
    if (flag == true) {
        var anchor = new qq.maps.Point(10, 30),
                origin = new qq.maps.Point(left, 0),
                size = new qq.maps.Size(27, 33),
                icon = new qq.maps.MarkerImage("./img/marker10.png", size, origin, anchor);
        marker.setIcon(icon);
    } else {
        var anchor = new qq.maps.Point(10, 30),
                origin = new qq.maps.Point(left, 35),
                size = new qq.maps.Size(27, 33),
                icon = new qq.maps.MarkerImage("./img/marker10.png", size, origin, anchor);
        marker.setIcon(icon);
    }
}
function setCurrent(arr, index, isMarker) {
    if (isMarker) {
        each(markerArray, function (n, ele) {
            if (n == index) {
                setAnchor(ele, false);
                ele.setZIndex(10);
            } else {
                if (!ele.isClicked) {
                    setAnchor(ele, true);
                    ele.setZIndex(9);
                }
            }
        });
    } else {
        each(markerArray, function (n, ele) {
            if (n == index) {
                ele.div.style.background = "#DBE4F2";
            } else {
                if (!ele.div.isClicked) {
                    ele.div.style.background = "#fff";
                }
            }
        });
    }
}
function setFlagClicked(arr, index) {
    each(markerArray, function (n, ele) {
        if (n == index) {
            ele.isClicked = true;
            ele.div.isClicked = true;
            var str = '<div style="width:250px;">' + ele.div.children[1].innerHTML.toString() + '</div>';
            var latLng = ele.getPosition();
            parent.document.getElementsByName("store.locationx")[0].value = latLng.getLat().toFixed(6);
			parent.document.getElementsByName("store.locationy")[0].value = latLng.getLng().toFixed(6);
            document.getElementById("poi_cur").value = latLng.getLat().toFixed(6) + "," + latLng.getLng().toFixed(6);
        } else {
            ele.isClicked = false;
            ele.div.isClicked = false;
        }
    });
}
var city = document.getElementById("city");

/*
curCity.onclick = function (e) {
    var e = e || window.event,
            target = e.target || e.srcElement;
    if (target.innerHTML == "更换城市") {
        city.style.display = "block";
        if(isNoValue){
            bside.innerHTML = "";
            each(markerArray, function (n, ele) {
                ele.setMap(null);
            });
            markerArray.length = 0;
        }

    }
};

var url2;
city.onclick = function (e) {
    var e = e || window.event,
            target = e.target || e.srcElement;
    if (target.className == "close") {
        city.style.display = "none";
    }
    if (target.className == "city_name") {

            curCity.children[0].innerHTML = target.innerHTML;
    
        url2 = encodeURI("http://apis.map.qq.com/ws/geocoder/v1/?region=" + curCity.children[0].innerHTML + "&address=" + curCity.children[0].innerHTML + "&key=K76BZ-W3O2Q-RFL5S-GXOPR-3ARIT-6KFE5&output=jsonp&&callback=?");
        $.getJSON(url2, function (result) {
            map.setCenter(new qq.maps.LatLng(result.result.location.lat, result.result.location.lng));
            map.setZoom(10);
        });
        city.style.display = "none";
    }
};

var url4;
$(".search_t").autocomplete({
    source:function(request,response){
        url4 = encodeURI("http://apis.map.qq.com/ws/place/v1/suggestion/?keyword=" + request.term + "&region=" + curCity.children[0].innerHTML + "&key=K76BZ-W3O2Q-RFL5S-GXOPR-3ARIT-6KFE5&output=jsonp&&callback=?");
        $.getJSON(url4,function(result){

            response($.map(result.data,function(item){
                return({
                    label:item.title

                })
            }));
        });
    }
});
*/
function each(obj, fn) {
    for (var n = 0, l = obj.length; n < l; n++) {
        fn.call(obj[n], n, obj[n]);
    }
}
</script>
</body>
</html>
