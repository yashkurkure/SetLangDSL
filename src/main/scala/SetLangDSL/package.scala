import scala.collection.mutable



package object SetLangDSL {

  def concatStrings(s: String*): String = {
    val string_builder = new mutable.StringBuilder

    s.foreach(s=>{
      string_builder.append(s)
    })
    
    string_builder.toString()
  }

}
