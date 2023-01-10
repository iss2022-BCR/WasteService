import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:sprint1_smart_device/view/view_about.dart';
import 'package:sprint1_smart_device/view/view_settings.dart';

import '../model/appl_message.dart';
import '../model/settings.dart';

import '../model/constants.dart' as Constants;

class NavDrawer extends StatelessWidget {
  const NavDrawer(
      {super.key, required this.settings, required this.saveSettings});

  final Settings settings;
  final Function(bool, String, ApplMessageType, String, String) saveSettings;

  @override
  Widget build(BuildContext context) {
    return Drawer(
      child: ListView(
        padding: EdgeInsets.zero,
        children: <Widget>[
          const DrawerHeader(
            decoration: BoxDecoration(
                image: DecorationImage(
                    fit: BoxFit.fill,
                    image: AssetImage('assets/imgs/Wall-E.jpg'))),
            child: Text(
              'Menu',
              style: TextStyle(color: Colors.white, fontSize: 25),
            ),
          ),
          /*ListTile(
            leading: Icon(Icons.input),
            title: Text('Welcome'),
            onTap: () => {},
          ),
          ListTile(
            leading: Icon(Icons.verified_user),
            title: Text('Profile'),
            onTap: () => {Navigator.of(context).pop()},
          ),*/
          ListTile(
            leading: const Icon(Icons.settings),
            title: const Text(Constants.titleSettings),
            onTap: () => {
              Navigator.push(
                  context,
                  MaterialPageRoute(
                      builder: (context) => ViewSettings(
                            settings: settings,
                            saveSettings: saveSettings,
                          )))
            },
          ),
          ListTile(
            leading: const Icon(Icons.info),
            title: const Text(Constants.titleAbout),
            onTap: () => {
              Navigator.push(
                  context, MaterialPageRoute(builder: (context) => ViewAbout()))
            },
          ),
          ListTile(
            leading: const Icon(Icons.exit_to_app),
            title: const Text('Quit'),
            onTap: () => {SystemNavigator.pop()},
          ),
        ],
      ),
    );
  }
}
