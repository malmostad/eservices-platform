<%@ include file="/WEB-INF/jspf/htmlTags.jspf" %>

<hst:link var="jqplot" path="/js/jqplot/jquery.jqplot.min.js"/>
<script language="javascript" src="${jqplot}" type="text/javascript"></script>

<hst:link var="jqplotcss" path="/js/jqplot/jquery.jqplot.css"/>
<link rel="stylesheet" href="${jqplotcss}" type="text/css"/>

<hst:link var="pieRenderer" path="/js/jqplot/plugins/jqplot.pieRenderer.min.js"/>
<script language="javascript" src="${pieRenderer}" type="text/javascript"></script>

<script type="text/javascript">


  $(document).ready(function(){

	  /*
  var ajaxDataRenderer = function(url, plot, options) {
    var ret = null;
     $.ajax({
       type: "GET",
       url: url,
    async: false,
    beforeSend: function(x) {},
    dataType: "json",
    success: function(data){
      ret = [[]];
      ret[0].push(['Enligt plan', data["DashOpenActivities"].onTrack]);
      ret[0].push(['Deadline inom 7 dagar', 2]); //data["DashOpenActivities"].atRisk]);      
      ret[0].push(['Försenad',  1]); // data["DashOpenActivities"].overdue]);
    },
    error: function(data, status, jqXHR) { 
      // alert("FAILED:" + status + " jqXHR" + jqXHR); 
    }
  });
   return ret;
};
*/

var dashData = [
               ['<fmt:message key="mycases.dash.ontrack.lbl"/>', ${dash.onTrack}], 
			   ['<fmt:message key="mycases.dash.atrisk.lbl"/>', ${dash.atRisk}], 
			   ['<fmt:message key="mycases.dash.overdue.lbl"/>',  ${dash.overdue}]
               ];

			   var plot2 = jQuery.jqplot ('chart2', [dashData],
					    {
					      seriesDefaults: {
					        renderer: jQuery.jqplot.PieRenderer,
					        rendererOptions: {
					          // Turn off filling of slices.
					          fill: false,
					          showDataLabels: true,
					          dataLabels:  'value',
					          // Add a margin to seperate the slices.
					          sliceMargin: 4,
					          // stroke the slices with a little thicker line.
					          lineWidth: 4,
					          seriesColors: ["green", "orange", "red"]
					        }
					      },
					      legend: { show:true, location: 'e' },
					      title: "<fmt:message key="mycases.dash.mypendingactivities.lbl"/>"
					    }
					  );
			   /*
var jsonurl =  "http://eservices.malmo.se/inherit-service-rest-server-1.0-SNAPSHOT/getDashOpenActivitiesByUserId/john/7?media=json";

  var plot2 = jQuery.jqplot ('chart2', jsonurl,
    {
      dataRenderer: ajaxDataRenderer,
      dataRendererOptions: {
        unusedOptionalUrl: jsonurl
      },
      seriesDefaults: {
        renderer: jQuery.jqplot.PieRenderer,
        rendererOptions: {
          // Turn off filling of slices.
          fill: false,
          showDataLabels: true,
          dataLabels:  'value',
          // Add a margin to seperate the slices.
          sliceMargin: 4,
          // stroke the slices with a little thicker line.
          lineWidth: 4,
          seriesColors: ["green", "orange", "red"]
        }
      },
      legend: { show:true, location: 'e' },
      title: "Mina pågående aktiviteter"
    }
  );
  */
});

</script>


<div id="chart2" ></div>
