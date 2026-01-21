$projectRoot = $PSScriptRoot
$srcDir = Join-Path $projectRoot "src"
$jarFile = Join-Path $projectRoot "biuoop-1.4.jar"
$outDir = Join-Path $projectRoot "bin"

# Create bin directory if it doesn't exist
if (-not (Test-Path $outDir)) {
    New-Item -ItemType Directory -Path $outDir | Out-Null
}

Write-Host "Compiling..." -ForegroundColor Cyan
# Compile all java files in src (recursively) to bin
# We include . in the classpath for local packages, and the jar
$classpath = "$jarFile;$srcDir"

# Get all java files
$javaFiles = Get-ChildItem -Path $srcDir -Recurse -Filter *.java | Select-Object -ExpandProperty FullName

if ($javaFiles) {
    javac -d $outDir -cp $classpath $javaFiles
    if ($LASTEXITCODE -eq 0) {
        Write-Host "Compilation successful. Running Game..." -ForegroundColor Green
        # Run
        # Classpath needs bin directory and the jar
        $runClasspath = "$jarFile;$outDir"
        java -cp $runClasspath Ass5Game
    } else {
        Write-Host "Compilation failed." -ForegroundColor Red
    }
} else {
    Write-Host "No Java files found in $srcDir" -ForegroundColor Yellow
}
