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
with Diagram('sprint3_wasteservice_analysisArch', show=False, outformat='png', graph_attr=graphattr) as diag:
  with Cluster('env'):
     sys = Custom('','./qakicons/system.png')
     with Cluster('ctx_wasteservice', graph_attr=nodeattr):
          typesprovider=Custom('typesprovider','./qakicons/symActorSmall.png')
          wasteservice=Custom('wasteservice','./qakicons/symActorSmall.png')
     with Cluster('ctx_transporttrolley', graph_attr=nodeattr):
          transporttrolley=Custom('transporttrolley','./qakicons/symActorSmall.png')
          trolleystateprovider=Custom('trolleystateprovider','./qakicons/symActorSmall.png')
          pathexecutorbcr=Custom('pathexecutorbcr','./qakicons/symActorSmall.png')
     with Cluster('ctx_robot', graph_attr=nodeattr):
          basicrobot=Custom('basicrobot','./qakicons/symActorSmall.png')
     with Cluster('ctx_raspberrypi', graph_attr=nodeattr):
          sonar_bcr=Custom('sonar_bcr(ext)','./qakicons/externalQActor.png')
          led_bcr=Custom('led_bcr(ext)','./qakicons/externalQActor.png')
          buzzer_bcr=Custom('buzzer_bcr(ext)','./qakicons/externalQActor.png')
          textdisplay_bcr=Custom('textdisplay_bcr(ext)','./qakicons/externalQActor.png')
     with Cluster('ctx_statusgui', graph_attr=nodeattr):
          gui_controller=Custom('gui_controller','./qakicons/symActorSmall.png')
          gui_simulator=Custom('gui_simulator','./qakicons/symActorSmall.png')
     wasteservice >> Edge(color='magenta', style='solid', xlabel='deposit', fontcolor='magenta') >> transporttrolley
     transporttrolley >> Edge(color='magenta', style='solid', xlabel='dopath', fontcolor='magenta') >> pathexecutorbcr
     transporttrolley >> Edge(color='darkgreen', style='dashed', xlabel='pickupcompleted', fontcolor='darkgreen') >> wasteservice
     transporttrolley >> Edge(color='blue', style='solid', xlabel='depositcompleted', fontcolor='blue') >> wasteservice
     transporttrolley >> Edge(color='blue', style='solid', xlabel='coapUpdate', fontcolor='blue') >> trolleystateprovider
     pathexecutorbcr >> Edge(color='blue', style='solid', xlabel='coapUpdate', fontcolor='blue') >> trolleystateprovider
     trolleystateprovider >> Edge( xlabel='trolley_state_changed', **eventedgeattr, fontcolor='red') >> sys
     sys >> Edge(color='red', style='dashed', xlabel='stop', fontcolor='red') >> pathexecutorbcr
     pathexecutorbcr >> Edge(color='blue', style='solid', xlabel='update_trolley_position', fontcolor='blue') >> gui_controller
     pathexecutorbcr >> Edge(color='magenta', style='solid', xlabel='step', fontcolor='magenta') >> basicrobot
     pathexecutorbcr >> Edge(color='blue', style='solid', xlabel='cmd', fontcolor='blue') >> basicrobot
     pathexecutorbcr >> Edge(color='darkgreen', style='dashed', xlabel='dopathdone', fontcolor='darkgreen') >> transporttrolley
     pathexecutorbcr >> Edge(color='darkgreen', style='dashed', xlabel='dopathfail', fontcolor='darkgreen') >> transporttrolley
     sys >> Edge(color='red', style='dashed', xlabel='resume', fontcolor='red') >> pathexecutorbcr
     basicrobot >> Edge(color='darkgreen', style='dashed', xlabel='stepdone', fontcolor='darkgreen') >> pathexecutorbcr
     basicrobot >> Edge(color='darkgreen', style='dashed', xlabel='stepfail', fontcolor='darkgreen') >> pathexecutorbcr
     sys >> Edge(color='red', style='dashed', xlabel='trolley_state_changed', fontcolor='red') >> gui_controller
     gui_controller >> Edge(color='blue', style='solid', xlabel='update_statusgui', fontcolor='blue') >> gui_simulator
diag
