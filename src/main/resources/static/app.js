var app = (function () {

    class Point {
        constructor(x, y) {
            this.x = x;
            this.y = y;
        }
    }

    var id=0;
    var stompClient = null;


    var puPoint = function (px, py) {
        var pt = new Point(px, py);
        console.info("publishing point at " + pt);
        addPointToCanvas(pt);
        stompClient.send("/topic/newpoint", {}, JSON.stringify({pt}));
        //publicar el evento
    }

    var addPointToCanvas = function (point) {

        var canvas = document.getElementById("canvas");
        var ctx = canvas.getContext("2d");
        ctx.beginPath();
        ctx.arc(point.x, point.y, 3, 0, 2 * Math.PI);
        ctx.stroke();
    };


    var getMousePosition = function (evt) {
        canvas = document.getElementById("canvas");
        var rect = canvas.getBoundingClientRect();
        return {
            x: evt.clientX - rect.left,
            y: evt.clientY - rect.top,

        };
    };


    var connectAndSubscribe = function (topic) {
        console.info('Connecting to WS...');
        var socket = new SockJS('/stompendpoint');
        stompClient = Stomp.over(socket);

        //subscribe to /topic/TOPICXX when connections succeed
        stompClient.connect({}, function (frame) {
            console.log('Connected: ' + frame);
            //alert("lolo2"+topic);
            stompClient.subscribe('/topic/newpoint'+topic, function (eventbody) {
                //alert("" + eventbody);
                
                var point = JSON.parse(eventbody.body);
                addPointToCanvas(new Point(point.x, point.y));
                //alert("se pinto");

            });
        });
    };

    return {

        init: function () {
            var can = document.getElementById("canvas");
            connectAndSubscribe('');
            //websocket connection

        },
        conectedSpecific: function () {
            var can = document.getElementById("canvas");
            id=document.getElementById("identificador").value;
            //alert("que es id "+id);
            connectAndSubscribe("."+id);

            canvas.addEventListener("mousedown", function (e) {
                point = getMousePosition(e);
                addPointToCanvas(point);
                stompClient.send("/app/newpoint."+id, {}, JSON.stringify(point));  
            })
        },
        publishPoint: function (px, py) {
            var pt = new Point(px, py);
            console.info("publishing point at " + pt);
            addPointToCanvas(pt);
            canvas.addEvenListener('click', function (evet) {
                var pos = getMousePosition(evet);
                stompClient.send("/topic/newpoint" + pos, {}, JSON.stringify(pos));
            }) //publicar el evento
        },

        disconnect: function () {
            if (stompClient !== null) {
                stompClient.disconnect();
            }
            setConnected(false);
            console.log("Disconnected");
        }
    };

})();