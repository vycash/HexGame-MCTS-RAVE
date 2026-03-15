$SRC_DIR = "src"
$BUILD_DIR = "build"
$LIB_DIR = "lib"
$MAIN_CLASS = "src/model.Main"

if (-not (Test-Path $BUILD_DIR)) {
    New-Item -ItemType Directory -Path $BUILD_DIR | Out-Null
}

$CLASSPATH = "$LIB_DIR\*"

Get-ChildItem -Path $SRC_DIR -Filter "*.java" -Recurse | ForEach-Object {
    & javac -d $BUILD_DIR -cp "$CLASSPATH" $_.FullName 2>$null
}

& java -cp "$BUILD_DIR;$CLASSPATH" $MAIN_CLASS GRAPHIQUE 2>$null