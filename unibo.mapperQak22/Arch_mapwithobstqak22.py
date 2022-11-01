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
with Diagram('mapwithobstqak22Arch', show=False, outformat='png', graph_attr=graphattr) as diag:
  with Cluster('env'):
     sys = Custom('','./qakicons/system.png')
     with Cluster('ctxbasicrobot', graph_attr=nodeattr):
          basicrobot=Custom('basicrobot(ext)','./qakicons/externalQActor.png')
          pathexec=Custom('pathexec(ext)','./qakicons/externalQActor.png')
     with Cluster('ctxmapwithobstqak22', graph_attr=nodeattr):
          mapwithobstqak22=Custom('mapwithobstqak22','./qakicons/symActorSmall.png')
     mapwithobstqak22 >> Edge(color='magenta', style='solid', xlabel='step') >> basicrobot
     mapwithobstqak22 >> Edge(color='blue', style='solid', xlabel='cmd') >> basicrobot
diag
