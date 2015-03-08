function get() {
  line=$1

  arr=$(echo $line | tr " " "." | tr "=" "\n")
  i=0
  for x in $arr
  do
    if [ $i == 1 ]; then
      if [[ $x =~ ^-?[0-9]+$ ]]
      then
        echo "int"
        return 0
      elif [ $x == "true" ] || [ "$x" == "false" ]
      then
        echo "boolean"
        return 0
      else
        echo "String"
        return 0
      fi
    fi
    i=$(($i+1))
  done
  
  echo "String"
  return 0
}

echo "Auto-building default values for configuration"
i=0
rm "./src/ch/aiko/pokemon/settings/Default.java"
touch "./src/ch/aiko/pokemon/settings/Default.java"
echo "package ch.aiko.pokemon.settings;" >> "./src/ch/aiko/pokemon/settings/Default.java"
echo "" >> "./src/ch/aiko/pokemon/settings/Default.java"
echo "public class Default {" >> "./src/ch/aiko/pokemon/settings/Default.java"
echo "" >> "./src/ch/aiko/pokemon/settings/Default.java"
while read line
do
  type=$(get $line)
  echo "    public static final $type $line;" >> "./src/ch/aiko/pokemon/settings/Default.java"
  i=$(($i+1))
done < "./assets/settings/fields"
echo "" >> "./src/ch/aiko/pokemon/settings/Default.java"
echo "}" >> "./src/ch/aiko/pokemon/settings/Default.java"

echo "Done"