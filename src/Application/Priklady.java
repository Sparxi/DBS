package Application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Priklady {
    Connection c = null;
    ArrayList<String> zoznam1 = new ArrayList<String>();
    ArrayList<String> zoznam2 = new ArrayList<String>();
    int pocet;
    public Priklady(Connection con) {
        c = con;
    }

    public int pocet_prikladov(String meno, short typ) throws SQLException{
        String sql_query;
        PreparedStatement pst = null;
        ResultSet res = null;

        sql_query = "SELECT count(*) FROM problems WHERE name = ? AND type_id = ?";
        pst = c.prepareStatement(sql_query);
        pst.setString(1, meno);
        pst.setShort(2, typ);
        res = pst.executeQuery();
        res.next();
        return (res.getInt(1));
    }
    public int najdi_priklad(String meno, short typ) throws SQLException {
        String sql_query;
        PreparedStatement pst = null;
        ResultSet res = null;

        sql_query = "SELECT * FROM problems WHERE problems.name = ? AND problems.type_id = ?";
        pst = c.prepareStatement(sql_query);
        pst.setString(1, meno);
        pst.setShort(2, typ);
        res = pst.executeQuery();
        if(res.next())
            return 1;
        else
            return 0;

    }
    public void vymaz_priklad(String klik) throws SQLException {
        String sql_query, s;
        PreparedStatement pst = null;
        ResultSet res = null;

        sql_query = "DELETE FROM problems WHERE problems.name = ? ";
        pst = c.prepareStatement(sql_query);
        pst.setString(1, klik);
        pst.execute();
    }
    public void vyhladaj_priklady(String vstup, String typ, String sort, int kolko, int odkial) throws SQLException {
        String sql_query, s, zaciatok;
        PreparedStatement pst = null;
        ResultSet res = null;

        StringBuilder sb = new StringBuilder(vstup);
        sb.insert(0,'%');
        sb.append('%');
        s = sb.toString();

        zaciatok = "SELECT problems.name, (avg(points) * 100 / max_points) AS priemer, count(problems.name) AS pocet" +
                " FROM problems JOIN problem_types ON problems.type_id = problem_types.id " +
                " FULL JOIN problems_students ON problems.id = problems_students.problem_id " +
                " WHERE LOWER(problems.name) like ?";
        StringBuilder sql = new StringBuilder(zaciatok);

        if(typ.equals("Všetky")){
            if (sort.equals("Úspešnosti - zostupne")) {
                sql.append(" GROUP BY problems.id ORDER BY priemer desc NULLS LAST OFFSET ? LIMIT ?");
            }
            if (sort.equals("Úspešnosti - vzostupne")) {
                sql.append(" GROUP BY problems.id ORDER BY priemer asc NULLS LAST OFFSET ? LIMIT ?");
            }
            if (sort.equals("Názvu príkladu - zostupne")) {
                sql.append(" GROUP BY problems.id ORDER BY problems.name desc NULLS LAST OFFSET ? LIMIT ?");
            }
            if (sort.equals("Názvu príkladu - vzostupne")) {
                sql.append(" GROUP BY problems.id ORDER BY problems.name asc NULLS LAST OFFSET ? LIMIT ?");
            }
            sql_query = sql.toString();
            pst = c.prepareStatement(sql_query);
            pst.setString(1, s.toLowerCase());
            pst.setInt(2, odkial);
            pst.setInt(3, kolko);
        }
        else {
            if (sort.equals("Úspešnosti - zostupne")) {
                sql.append(" AND problem_types.name = ? GROUP BY problems.id ORDER BY priemer desc NULLS LAST OFFSET ? LIMIT ?");
            }
            if (sort.equals("Úspešnosti - vzostupne")) {
                sql.append(" AND problem_types.name = ? GROUP BY problems.id ORDER BY priemer asc NULLS LAST OFFSET ? LIMIT ?");
            }
            if (sort.equals("Názvu príkladu - zostupne")) {
                sql.append(" AND problem_types.name = ? GROUP BY problems.id ORDER BY problems.name desc NULLS LAST OFFSET ? LIMIT ?");
            }
            if (sort.equals("Názvu príkladu - vzostupne")) {
                sql.append(" AND problem_types.name = ? GROUP BY problems.id ORDER BY problems.name asc NULLS LAST OFFSET ? LIMIT ?");
            }
            sql_query = sql.toString();
            pst = c.prepareStatement(sql_query);
            pst.setString(1, s.toLowerCase());
            pst.setString(2, typ);
            pst.setInt(3, odkial);
            pst.setInt(4, kolko);
        }
        res = pst.executeQuery();
        while (res.next()){
            zoznam1.add(res.getString(1));
            if(res.getString(2) == null)
                zoznam2.add(" ");
            else
               zoznam2.add(res.getString(2));
        }

    }
    public short najdi_id(String typ_kurzu) throws Exception{
        String sql_query;
        PreparedStatement pst = null;
        ResultSet res = null;
        short id = 0;

        sql_query = "SELECT id FROM problem_types WHERE problem_types.name = ?";
        pst = c.prepareStatement(sql_query);
        pst.setString(1, typ_kurzu);

        res = pst.executeQuery();
        if(res.next()) {
            id = res.getShort("id");
            return id;
        }
        else
            return 0;
    }
    public int pridaj_priklad(String nazov, float vysledok, int body, String znenie, String typ) throws Exception {
        String sql_query;
        PreparedStatement pst = null;
        ResultSet res = null;
        short id = this.najdi_id(typ);
        c.setAutoCommit(false);
        sql_query = "INSERT INTO problems(type_id, name, content, result, max_points) VALUES (?, ?, ?, ?, ?)";
        pst = c.prepareStatement(sql_query);
        pst.setShort(1, id);
        pst.setString(2, nazov);
        pst.setString(3, znenie);
        pst.setFloat(4, vysledok);
        pst.setInt(5, body);

        if(this.najdi_priklad(nazov,id) == 1){
            pst.execute();
            System.out.println("Nepodarilo sa pridat priklad");
            c.rollback();
            return 1;
        }
        else {
            pst.execute();
            System.out.println("Priklad pridany");
            c.commit();
            return 0;
        }
    }
    public ArrayList<String> getZoznam2() {
        return zoznam2;
    }
    public ArrayList<String> getZoznam1() {
        return zoznam1;
    }
}
