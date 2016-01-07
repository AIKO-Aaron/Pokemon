cd "$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cp ../AikoUtil.jar .
cp ~/Dropbox/Public/Pokemon.jar ./builds/newest.jar
echo "Commit message:"
git add .
read commit
git commit -m "$commit"
git push -u origin master
