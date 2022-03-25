import SetLangDSL.*

import scala.collection.mutable

object myMain {


  class A{
    val set  = mutable.Set.empty[String]

    def this(a: A)={
      this()
      a.set.foreach(s=>this.set.add(s))
    }

    def insert(s: String): Unit = {
      set.add(s)
    }

    def deepCopy():A = {
      new A(this)
    }
  }



  @main
  def main(): Unit = {

    val x = new A()
    x.insert("string 1")
    val y = x.deepCopy()
    y.insert("string 2")
    x.insert("string 3")

    println(x.set)
    println(y.set)

  }

}
