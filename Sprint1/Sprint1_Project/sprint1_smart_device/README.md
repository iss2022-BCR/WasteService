# sprint1_smart_device


## Build
`flutter build a3a33c8a0d9adc920e9decpk --split-per-abi --no-sound-null-safety`

## Add INTERNET Permission
To have socket support on a real device we need to give the [networking permissions](https://docs.flutter.dev/development/data-and-backend/networking):
in **/android/app/src/main/AndroidManifest.xml** add the following line:
```xml
<uses-permission android:name="android.permission.INTERNET"/>
```

## Generate UML Diagrams
1. Install [`dcdg`](https://pub.dev/packages/dcdg) package
```
flutter pub add dcdg
```
2. in `main.dart` import the package:
```
import 'package:dcdg/dcdg.dart';
```
3. Install PlantUML extension
```lang-none
Extensions > PlantUML > Install
```
4. Activate 
```
flutter pub global activate dcdg
```
5. Run
```
flutter pub global run dcdg
```

## References

- [Flutter Form Validation](https://docs.flutter.dev/cookbook/forms/validation)
- [AlertDialog class](https://api.flutter.dev/flutter/material/AlertDialog-class.html)
- [always scroll at the end of ListView](https://stackoverflow.com/a/63947715/19544859)
- [dart JSON](https://api.flutter.dev/flutter/dart-convert/jsonDecode.html)