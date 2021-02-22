<template>
  <div>
    <Navbar
      style="position:sticky; top:0; z-index:30; width: 100%;"
    >
    </Navbar>
    <div class="map_wrap">
      <div id="map" style="width:100%;height:100%;position:relative;overflow:hidden;">
        <div class ="select">
          <button @click="toggleShowBtn" :class="{'buttonsHideBtn': !this.showBtn, 'buttonsHideBtnNone': this.showBtn}"><i class="fas fa-map-pin"></i></button>
          <div :class="{'buttons': this.showBtn, 'hideButtons': !this.showBtn}">
            <div @click="toggleShowBtn" class="my-position">
              <i class="fas fa-map-pin"></i>  {{this.city}} {{this.gu}}
            <hr>
            </div>
            <button @click="searchHospitals"> <img class="icon-image" src="@/assets/images/icons/hospital.png"> 동물병원 </button>
            <button @click="searchCafes"> <img class="icon-image" src="@/assets/images/icons/cafe.png"> 애견카페 </button>
            <button @click="searchHotels"> <img class="icon-image" src="@/assets/images/icons/hotel.png"> 애견호텔 </button>
          </div>
        </div>
      </div>
      <button @click="togglePlaceList" :class="{'hideBtn': this.showPlaceList, 'showingBtn': !this.showPlaceList}">
        <i class="fas fa-caret-right"></i>
      </button>
      <div :class="{'placeListWrap': this.showPlaceList, 'placeListWrapNone': !this.showPlaceList}">
        <div id="menu_wrap" class="bg_white">
          <div class="toggle-place-list" @click="togglePlaceList">
            리스트 숨기기
          </div>
          <ul id="placesList"></ul>
          <div id="pagination"></div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import Navbar from "../../components/Nav/Navbar.vue";
    export default {
        data: function () {
                return {
                    markers: [],
                    selected:"동물병원",
                    city: "",
                    gu:"",
                    showPlaceList: true,
                    showBtn: true,
                };
            },
         components: {
            Navbar
        },
        mounted() {
            window.kakao && window.kakao.maps ? this.initMap() : this.addScript();
        }

        ,
        methods: {
          toggleShowBtn: function () {
            this.showBtn = !this.showBtn
          },
          togglePlaceList: function () {
            this.showPlaceList = !this.showPlaceList
          },
            initMap() {
                var container = document.getElementById('map');

                var options = {
                    center: new kakao.maps.LatLng(37.552081, 126.988322),
                    level: 5, // 확대 레벨
                };
                // 지도 생성 -
                var map = new kakao.maps.Map(container, options);
        
                // this.getNowplace(map);
                let center; 
                
                if (navigator.geolocation) {
                
                    navigator.geolocation.getCurrentPosition((position)=> {
                        var lat = position.coords.latitude, // 위도
                            lon = position.coords.longitude; // 경도
                        var locPosition = new kakao.maps.LatLng(lat, lon);
                        map.setCenter(locPosition);    
                        center = map.getCenter();
                        // console.log(map.getCenter());
                        var geocoder = new kakao.maps.services.Geocoder();
                        map = new kakao.maps.LatLng(center.La, center.Ma);
                        var callback = (result, status)=> {
                            if (status === kakao.maps.services.Status.OK) {
                                // console.log(result[0].address.region_1depth_name);
                                // console.log(result[0].address.region_2depth_name);
                                // console.log(result[0].address.region_3depth_name);
                                // console.log('주소 : ' + result[0].address.address_name);
                                this.city = result[0].address.region_1depth_name;
                                this.gu = result[0].address.region_2depth_name;

                this.searchPlaces();
                            } 
                        };
                        geocoder.coord2Address(center.La, center.Ma, callback);
                    });
                }
            },
            
            searchPlaces() {
                
                var ps = new kakao.maps.services.Places();
                // console.log(this.city)
                // console.log(this.gu)
                // console.log(this.selected)
                var keyword = this.city+this.gu+this.selected;
                // console.log("키워드 : "+keyword)
                ps = new kakao.maps.services.Places();

                if (!keyword.replace(/^\s+|\s+$/g, '')) {
                    alert('키워드를 입력해주세요!');
                    return false;
                }

                // 장소검색 객체를 통해 키워드로 장소검색을 요청합니다
                ps.keywordSearch(keyword, this.placesSearchCB);
            },

            // 장소검색이 완료됐을 때 호출되는 콜백함수 입니다
            placesSearchCB(data, status, pagination) {
                if (status === kakao.maps.services.Status.OK) {

                    // 정상적으로 검색이 완료됐으면
                    // 검색 목록과 마커를 표출합니다
                    this.displayPlaces(data);

                    // 페이지 번호를 표출합니다
                    this.displayPagination(pagination);

                } else if (status === kakao.maps.services.Status.ZERO_RESULT) {

                    alert('검색 결과가 존재하지 않습니다.');
                    return;

                } else if (status === kakao.maps.services.Status.ERROR) {

                    alert('검색 결과 중 오류가 발생했습니다.');
                    return;

                }
            }

            ,

            // 검색 결과 목록과 마커를 표출하는 함수입니다
            displayPlaces(places) {

                var container = document.getElementById('map');

                var options = {
                    center: new kakao.maps.LatLng(37.552081, 126.988322),
                    level: 5, // 확대 레벨
                }

                ;
                // 지도 생성 -
                var map = new kakao.maps.Map(container, options);
                var listEl = document.getElementById('placesList'),
                    menuEl = document.getElementById('menu_wrap'),
                    fragment = document.createDocumentFragment(),
                    bounds = new kakao.maps.LatLngBounds();

                // 검색 결과 목록에 추가된 항목들을 제거합니다
                this.removeAllChildNods(listEl);

                // 지도에 표시되고 있는 마커를 제거합니다
                this.removeMarker();

                for (var i = 0; i < places.length; i++) {

                    // 마커를 생성하고 지도에 표시합니다
                    var placePosition = new kakao.maps.LatLng(places[i].y, places[i].x);
                    var marker = this.addMarker(placePosition, i, map);
                    var itemEl = this.getListItem(i, places[i]); // 검색 결과 항목 Element를 생성합니다

                    // 검색된 장소 위치를 기준으로 지도 범위를 재설정하기위해
                    // LatLngBounds 객체에 좌표를 추가합니다
                    bounds.extend(placePosition);

                    fragment.appendChild(itemEl);
                }

                // 검색결과 항목들을 검색결과 목록 Elemnet에 추가합니다
                listEl.appendChild(fragment);
                menuEl.scrollTop = 0;

                // 검색된 장소 위치를 기준으로 지도 범위를 재설정합니다
                map.setBounds(bounds);
            }

            ,

            // 검색결과 항목을 Element로 반환하는 함수입니다
            getListItem(index, places) {

                var el = document.createElement('li'),
                    itemStr = '<span class="markerbg marker_' + (index + 1) + '"></span>' + '<div class="info">' +
                    '   <h5>' + places.place_name + '</h5>';

                if (places.road_address_name) {
                    itemStr += '    <span>' + places.road_address_name + '</span>' + '   <span class="jibun gray">' +
                        places.address_name + '</span>';
                } else {
                    itemStr += '    <span>' + places.address_name + '</span>';
                }

                itemStr += '  <span class="tel">' + places.phone + '</span>' + '</div>';

                el.innerHTML = itemStr;
                el.className = 'item';

                return el;
            }

            ,
            // 여기
            // 마커를 생성하고 지도 위에 마커를 표시하는 함수입니다
            addMarker(position, idx, map) {

                var imageSrc = 'https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/marker_number_blue.png',
                    // 마커 이미지 url, 스프라이트 이미지를 씁니다
                    imageSize = new kakao.maps.Size(36, 37),
                    // 마커 이미지의 크기
                    imgOptions = {
                        spriteSize: new kakao.maps.Size(36, 691), // 스프라이트 이미지의 크기
                        spriteOrigin: new kakao.maps.Point(0, (idx * 46) + 10), // 스프라이트 이미지 중 사용할 영역의 좌상단 좌표
                        offset: new kakao.maps.Point(13, 37) // 마커 좌표에 일치시킬 이미지 내에서의 좌표
                    }

                    ,
                    markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize, imgOptions),
                    marker = new kakao.maps.Marker({
                            position: position, // 마커의 위치
                            image: markerImage
                        }

                    );

                marker.setMap(map); // 지도 위에 마커를 표출합니다
                this.markers.push(marker); // 배열에 생성된 마커를 추가합니다

                return marker;
            }

            ,

            // 지도 위에 표시되고 있는 마커를 모두 제거합니다
            removeMarker() {
                for (var i = 0; i < this.markers.length; i++) {
                    this.markers[i].setMap(null);
                }

                this.markers = [];
            }

            ,

            // 검색결과 목록 하단에 페이지번호를 표시는 함수입니다
            displayPagination(pagination) {
                var paginationEl = document.getElementById('pagination'),
                    fragment = document.createDocumentFragment(),
                    i;

                // 기존에 추가된 페이지번호를 삭제합니다
                while (paginationEl.hasChildNodes()) {
                    paginationEl.removeChild(paginationEl.lastChild);
                }

                for (i = 1; i <= pagination.last; i++) {
                    var el = document.createElement('a');
                    el.href = "#";
                    el.innerHTML = i;

                    if (i === pagination.current) {
                        el.className = 'on';
                    } else {
                        el.onclick = (function (i) {
                                return function () {
                                    pagination.gotoPage(i);
                                }
                            }

                        )(i);
                    }

                    fragment.appendChild(el);
                }

                paginationEl.appendChild(fragment);
            }

            ,


            // 검색결과 목록의 자식 Element를 제거하는 함수입니다
            removeAllChildNods(el) {
                while (el.hasChildNodes()) {
                    el.removeChild(el.lastChild);
                }
            }

            ,
            addScript() {
                const script = document.createElement('script');
                /* global kakao */
                script.onload = () => kakao.maps.load(this.initMap);
                script.src =
                  'http://dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=27d3b2e72d38922878610f195feb1f3c&libraries=services';
                document.head.appendChild(script);

            },
            searchHospitals(){
                this.selected = "동물병원";
                this.searchPlaces();
            },
            searchCafes(){
                this.selected = "애견카페";
                this.searchPlaces();
            },
            searchHotels(){
                this.selected = "애견호텔";
                this.searchPlaces();
            }
        }

        ,

    }
</script>
<style type="text/css">
@font-face {
  font-family: 'GmarketSansMedium';
  src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_2001@1.1/GmarketSansMedium.woff') format('woff');
  font-weight: normal;
  font-style: normal;
}

.buttonsHideBtn {
  z-index: 5;
  background-color: beige;
  color: black;
  width: 40px;
  height: 40px;
  border-radius: 70%;
  opacity: 0.5;
  transition-duration: 0.5s;
  display: flex;
  justify-content: center;
  align-items: center;
  transform: translate(0, 50%);
  -webkit-transform: translate(0, 50%);
  -ms-transform: translate(0, 50%);
}

.buttonsHideBtn:hover {
  opacity: 0.9;
}

.buttonsHideBtnNone {
  transition-duration: 0.5s;
  transform: translate(0, -150%);
  -webkit-transform: translate(0, -150%);
  -ms-transform: translate(0, -150%);
}

.buttonsHideBtn svg {
  font-size: 25px;
}

.toggle-place-list {
  z-index: 99;
}

.toggle-place-list:hover {
  cursor: pointer;
}

.showingBtn {
  transition-duration: 1s;
  position: fixed;
  top: 47.5%;
  height: 5%;
  left: 2%;
  width: 40px;
  background-color: beige;
  opacity: 0.5;
  color: black;
  font-size: 10vw;
  z-index: 3;
}

.showingBtn:hover {
  opacity: 0.9;
}

.showingBtn svg {
  font-size: 45px;
}

.placeListWrapNone {
  transition-duration: 0.5s;
  transform: translate(-150%, 0);
  -webkit-transform: translate(-150%, 0);
  -ms-transform: translate(-150%, 0);
}

.placeListWrap {
  transition-duration: 0.5s;
  /* position: unset; */
}

.map_wrap,
.map_wrap * {
  margin: 0;
  padding: 0;
  font-family: 'GmarketSansMedium';
  font-size: 12px;
}

.map_wrap a,
.map_wrap a:hover,
.map_wrap a:active {
  color: #000;
  text-decoration: none;
}

.map_wrap {
  position: relative;
  width: 100%;
  height: 800px;
  z-index: 20;
}
.select {
  position : relative;
  display: flex;
  justify-content: center;
  width: 100%;
  z-index: 15;
}

.buttons {
  background-color: rgb(255, 255, 255);
  border-radius:10px;
  border-color:rgb(119, 119, 119);
  box-shadow: 3px 3px 3px grey;
  margin:1.5vw;
  min-width: 250px;
  padding: 5px;
  position : absolute;
  left: 40%;
  top: 4vh;
  transition-duration: 0.5s;
  transform: translate(-15%,-50%);
}

.hideButtons {
  display: none;
  transition-duration: 0.5s;
  transform: translate(0, -150%);
  -webkit-transform: translate(0, -150%);
  -ms-transform: translate(0, -150%);
}

.buttons button {
  min-width: 60px;
}

.my-position {
  font-size : 1.3vw;
  padding : 0.1vw;
  color:rgb(148, 148, 148);
  text-align: center; 
}

.my-position:hover {
  cursor: pointer;
}

.icon-image{
  width : 2vw;
  height : 2vw;
}

.buttons button {
  width: 12vw;
  padding: 0.3vw;
  margin : 0.3vw;
  border-radius: 1vw;
  background-color: white;
  color:rgb(148, 148, 148);
  font-size: 1.5vw;
  font-weight: bold;
  transition-duration: 0.5s;
  }

#menu_wrap {
  position: absolute;
  top: 4vw;
  left: 0;
  bottom: 0;
  width: 250px;
  margin: 10px 0 30px 10px;
  padding: 5px;
  overflow-y: auto;
  background: rgba(255, 255, 255, 0.7);
  z-index: 10;
  font-size: 12px;
  border-radius: 10px;
}

.bg_white {
  background: #fff;
}

#menu_wrap hr {
  display: block;
  height: 1vw;
  border: 0;
  border-top: 2px solid #5F5F5F;
  margin: 3px 0;
}

#menu_wrap .option {
  text-align: center;
}

#menu_wrap .option p {
  margin: 10px 0;
}

#menu_wrap .option button {
  margin-left: 5px;
}

#placesList li {
  list-style: none;
}

#placesList .item {
  position: relative;
  border-bottom: 1px solid #888;
  overflow: hidden;
  cursor: pointer;
  min-height: 65px;
}

#placesList .item span {
  display: block;
  margin-top: 4px;
}

#placesList .item h5,
#placesList .item .info {
  text-overflow: ellipsis;
  overflow: hidden;
  white-space: nowrap;
}

#placesList .item .info {
  padding: 10px 0 10px 55px;
}

#placesList .info .gray {
  color: #8a8a8a;
}

#placesList .info .jibun {
  padding-left: 26px;
  background: url(https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/places_jibun.png) no-repeat;
}

#placesList .info .tel {
  color: #009900;
}

#placesList .item .markerbg {
  float: left;
  position: absolute;
  width: 36px;
  height: 37px;
  margin: 10px 0 0 10px;
  background: url(https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/marker_number_blue.png) no-repeat;
}

#placesList .item .marker_1 {
  background-position: 0 -10px;
}

#placesList .item .marker_2 {
  background-position: 0 -56px;
}

#placesList .item .marker_3 {
  background-position: 0 -102px
}

#placesList .item .marker_4 {
  background-position: 0 -148px;
}

#placesList .item .marker_5 {
  background-position: 0 -194px;
}

#placesList .item .marker_6 {
  background-position: 0 -240px;
}

#placesList .item .marker_7 {
  background-position: 0 -286px;
}

#placesList .item .marker_8 {
  background-position: 0 -332px;
}

#placesList .item .marker_9 {
  background-position: 0 -378px;
}

#placesList .item .marker_10 {
  background-position: 0 -423px;
}

#placesList .item .marker_11 {
  background-position: 0 -470px;
}

#placesList .item .marker_12 {
  background-position: 0 -516px;
}

#placesList .item .marker_13 {
  background-position: 0 -562px;
}

#placesList .item .marker_14 {
  background-position: 0 -608px;
}

#placesList .item .marker_15 {
  background-position: 0 -654px;
}

#pagination {
  margin: 10px auto;
  text-align: center;
}

#pagination a {
  display: inline-block;
  margin-right: 10px;
  color: #777;
}

#pagination .on {
  font-weight: bold;
  cursor: default;
  color: black;
}

@media ( max-width: 540px) {

  .showingBtn {
    transition-duration: 1s;
    position: fixed;
    top: 47.5%;
    height: 50px;
    left: 2%;
    width: 40px;
    background-color: beige;
    opacity: 0.5;
    color: black;
    font-size: 10vw;
    z-index: 3;
  }

  .showingBtn svg {
    font-size: 45px;
  }

.map_wrap,
.map_wrap * {
  margin: 0;
  padding: 0;
  font-family: 'GmarketSansMedium';
  font-size: 10px;
}

.map_wrap a,
.map_wrap a:hover,
.map_wrap a:active {
  color: #000;
  text-decoration: none;
}

.map_wrap {
  position: relative;
  width: 100%;
  height: 800px;
}
.select{
  position : relative;
  width: 100%;
  /* height: 30px; */
  z-index: 5;
}

.buttons{
  background-color: rgb(255, 255, 255);
  border-radius:10px;
  border-color:rgb(119, 119, 119);
  box-shadow: 3px 3px 3px grey;
  margin:1.5vw;
  padding: 5px;
  position : absolute;
  left:20%;
  top:25px;
  transform: translate(-10%,-50%);
  text-align: center;
}  
.my-position{
  font-size : 2.5vw;
  padding : 0.1vw;
  color:rgb(148, 148, 148);
  text-align: center; 
}
.icon-image{
  width : 2.5vw;
  height : 2.5vw;
}
.buttons button {
  width: 14vw;
  padding: 0.3vw;
  margin : 0.3vw;
  border-radius: 1vw;
  background-color: white;
  color:rgb(148, 148, 148);
  font-size: 2.5vw;
  font-weight: bold;
  transition-duration: 0.5s;
}

#menu_wrap {
  position: absolute;
  top: 0;
  left: 0;
  bottom: 0;
  /* width: 42vw; */
  top: 30px;
  margin: 10px 0 20px 10px;
  padding: 5px;
  overflow-y: auto;
  background: rgba(255, 255, 255, 0.7);
  z-index: 10;
  font-size:12px;
  border-radius: 10px;
}

.bg_white {
  background: #fff;
}

#menu_wrap hr {
  display: block;
  height: 0.5vw;
  border: 0;
  border-top: 2px solid #5F5F5F;
  margin: 3px 0;
}

#menu_wrap .option {
  text-align: center;
}

#menu_wrap .option p {
  margin: 10px 0;
}

#menu_wrap .option button {
  margin-left: 5px;
}

#placesList li {
  list-style: none;
}

#placesList .item {
  position: relative;
  border-bottom: 1px solid #888;
  overflow: hidden;
  cursor: pointer;
  min-height: 50px;
}


}

</style>
