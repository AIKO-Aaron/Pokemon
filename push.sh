cd "$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cp ~/Dropbox/Public/Pokemon.jar ./builds/newest.jar
echo "Commit branch:"
read branch
git checkout "$branch"
echo "Commit message:"
git add .
read commit
git commit -m "$commit"
git push -u origin "$branch"
