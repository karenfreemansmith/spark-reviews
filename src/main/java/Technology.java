import org.sql2o.*;
import java.util.List;

public class Technology {
  private String name;
  private int id;

  public Technology(String name) {
    this.name = name;
    if(Technology.findByName(name)!=null) {
      Technology tempTech = Technology.findByName(name);
      this.id = tempTech.getId();
    } else {
      try(Connection cn = DB.sql2o.open()) {
        String sql = "INSERT INTO technologies (name) VALUES (:name)";
        this.id = (int) cn.createQuery(sql, true)
          .addParameter("name", this.name)
          .executeUpdate()
          .getKey();
      }
    }
  }

  public String getName() {
    return name;
  }

  public int getId() {
    return id;
  }

  public static Technology findById(int id) {
    try(Connection cn = DB.sql2o.open()) {
      String sql = "SELECT * FROM technologies WHERE id=:id";
      Technology tech = cn.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Technology.class);
      return tech;
    }
  }

  public static Technology findByName(String name) {
    try(Connection cn = DB.sql2o.open()) {
      String sql = "SELECT * FROM technologies WHERE name=:name";
      Technology tech = cn.createQuery(sql)
        .addParameter("name", name)
        .executeAndFetchFirst(Technology.class);
      return tech;
    }
  }

  public static List<Technology> all() {
    String sql = "SELECT * FROM technologies ORDER BY name";
    try(Connection cn = DB.sql2o.open()) {
      return cn.createQuery(sql).executeAndFetch(Technology.class);
    }
  }

  @Override
  public boolean equals(Object otherTech) {
    if (!(otherTech instanceof Technology)) {
      return false;
    } else {
      Technology newTech = (Technology) otherTech;
      return this.getName().equals(newTech.getName()) &&
             this.getId()==newTech.getId();
    }
  }
}