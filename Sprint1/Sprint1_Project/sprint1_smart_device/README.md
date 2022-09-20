# sprint1_smart_device


## Build
`flutter build a3a33c8a0d9adc920e9decpk --split-per-abi --no-sound-null-safety`

## Add INTERNET Permission
To have socket support on a real device we need to give the [networking permissions](https://docs.flutter.dev/development/data-and-backend/networking):
in **/android/app/src/main/AndroidManifest.xml** add the following line:
```xml
<uses-permission android:name="android.permission.INTERNET"/>
```

## References

- [Flutter Form Validation](https://docs.flutter.dev/cookbook/forms/validation)
- [AlertDialog class](https://api.flutter.dev/flutter/material/AlertDialog-class.html)
- [always scroll at the end of ListView](https://stackoverflow.com/a/63947715/19544859)
- [dart JSON](https://api.flutter.dev/flutter/dart-convert/jsonDecode.html)