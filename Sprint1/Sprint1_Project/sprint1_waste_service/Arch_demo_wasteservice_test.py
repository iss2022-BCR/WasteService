from diagrams import Cluster, Diagram, Edge
from diagrams.custom import Custom
import os
os.environ['PATH'] += os.pathsep + 'C:/Program Files/Graphviz/bin/'

graphattr = {     #https://www.graphviz.org/doc/info/attrs.html
    'fontsize': '22',
}

nodeattr = {   
    'fontsize': '22',
    'bgcolor': 'lightyellow'
}

eventedgeattr = {
    'color': 'red',
    'style': 'dotted'
}
with Diagram('demo_wasteservice_testArch', show=False, outformat='png', graph_attr=graphattr) as diag:
  with Cluster('env'):
     sys = Custom('','./qakicons/system.png')
     with Cluster('ctxwasteservice_test', graph_attr=nodeattr):
          wasteservice=Custom('wasteservice','./qakicons/symActorSmall.png')
          transporttrolley=Custom('transporttrolley','./qakicons/symActorSmall.png')
          trolleymover=Custom('trolleymover','./qakicons/symActorSmall.png')
          pather=Custom('pather','./qakicons/symActorSmall.png')
          basicrobot=Custom('basicrobot','./qakicons/symActorSmall.png')
          envsonarhandler=Custom('envsonarhandler','./qakicons/symActorSmall.png')
          datacleaner=Custom('datacleaner(coded)','./qakicons/codedQActor.png')
          distancefilter=Custom('distancefilter(coded)','./qakicons/codedQActor.png')
     wasteservice >> Edge(color='magenta', style='solid', xlabel='depositRequest') >> transporttrolley
     wasteservice >> Edge(color='green', style='dashed', xlabel='loadRejected') >> sys 
     wasteservice >> Edge(color='green', style='dashed', xlabel='loadAccepted') >> sys 
     wasteservice >> Edge(color='green', style='dashed', xlabel='loadRejected') >> sys 
     transporttrolley >> Edge(color='magenta', style='solid', xlabel='move') >> trolleymover
     transporttrolley >> Edge(color='green', style='dashed', xlabel='pickupDone') >> sys 
     transporttrolley >> Edge(color='green', style='dashed', xlabel='pickupDone') >> sys 
     trolleymover >> Edge(color='magenta', style='solid', xlabel='stopPath') >> pather
     trolleymover >> Edge(color='magenta', style='solid', xlabel='doPath') >> pather
     trolleymover >> Edge(color='green', style='dashed', xlabel='moveDone') >> sys 
     trolleymover >> Edge(color='green', style='dashed', xlabel='moveDone') >> sys 
     pather >> Edge(color='green', style='dashed', xlabel='stopAck') >> sys 
     pather >> Edge(color='blue', style='solid', xlabel='cmd') >> basicrobot
     pather >> Edge(color='magenta', style='solid', xlabel='step') >> basicrobot
     sys >> Edge(color='red', style='dashed', xlabel='alarm') >> pather
     pather >> Edge(color='green', style='dashed', xlabel='doPathDone') >> sys 
     pather >> Edge(color='green', style='dashed', xlabel='doPathFail') >> sys 
     basicrobot >> Edge(color='green', style='dashed', xlabel='stepdone') >> sys 
     basicrobot >> Edge(color='green', style='dashed', xlabel='stepfail') >> sys 
     sys >> Edge(color='red', style='dashed', xlabel='sonar') >> envsonarhandler
diag
