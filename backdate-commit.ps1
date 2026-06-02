# Backdate Commit Script (PowerShell)
# ------------------------------------------------------------
# This script creates a tiny dummy change and makes a Git commit
# with a back‑dated author/committer date.
#
# Usage:
#   powershell -ExecutionPolicy Bypass -File backdate-commit.ps1 [-WeeksAgo <n>]
#
# Parameters:
#   -WeeksAgo : Number of weeks before today to use for the commit date.
#               Default is 1 (i.e., 7 days ago).
#
# What the script does:
#   1. Computes the target date (current date minus WeeksAgo*7 days).
#   2. Stages all changes (git add .).
#   3. Runs `git commit` with GIT_AUTHOR_DATE and GIT_COMMITTER_DATE set to the back‑dated value.
#   4. (Optional) Pushes to the remote if you uncomment the push line.
#
# IMPORTANT: Run this script from the root of the Android project
#            (the directory that contains the .git folder).
# ------------------------------------------------------------

param(
    [int]$WeeksAgo = 1
)

# Compute back‑dated timestamp (ISO 8601 format expected by Git)
$targetDate = (Get-Date).AddDays(-7 * $WeeksAgo)
$gitDate = $targetDate.ToString("yyyy-MM-ddTHH:mm:sszzz")

Write-Host "Back‑dating commit to: $gitDate"

# Stage changes (make sure you have at least one change, e.g., the README edit)
git add .

# Set the back‑dated environment variables
$env:GIT_AUTHOR_DATE = $gitDate
$env:GIT_COMMITTER_DATE = $gitDate

git commit -m "Update project files"

# Uncomment the next line if you want to push automatically
# git push origin $(git rev-parse --abbrev-ref HEAD)

Write-Host "Commit created with back‑dated timestamp."
