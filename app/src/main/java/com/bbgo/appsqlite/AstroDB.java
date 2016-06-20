package com.bbgo.appsqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/* Classes que precisam criar ou abrir um banco de dados devem herdar de SQLiteOpenHelper */
public class AstroDB extends SQLiteOpenHelper {
    private static final String TAG = "sqlAstros";
    // Nome do banco
    public static final String NOME_BANCO = "astros.sqlite";
    private static final int VERSAO_BANCO = 1;

    //No construtor informamos o nome do banco de dados e a versão (código númerico)
    public AstroDB(Context context) {
        // context, nome do banco, factory, versão
        super(context, NOME_BANCO, null, VERSAO_BANCO);
    }

    /*Caso o banco não exista, o método onCreate(SQLiteDatabase) é chamado para executar um script
    sql com o objetivo de criar as tabelas no banco de dados*/
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "Criando a Tabela astro...");
        db.execSQL("create table if not exists astro (id integer primary key autoincrement,nome text, desc text, tipo text);");
        Log.d(TAG, "Tabela astro criada com sucesso.");
    }

    /*Caso a versão informada no construtor seja diferente da versão atual, o método onUpgrade é
    chamado. Neste método podemos executar algum script para atualizar a versão do banco de dados:
    criar novas tabelas, adicionar/alterar colunas, etc.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    // Insere um novo Astro, ou atualiza se já existe
    public long save(Astro astro) {
        long id = astro.getId();
        /*Aqui o SQL tenta criar um novo banco de dados e caso o banco não exista, o método
        onCreate(db) será chamado para o aplicativo executar algum SQL para criar as tabelas.
        Caso o parâmetro da versão que foi passado no construtor da classe seja diferente da versão
        atual, o método onUpgrade será chamado para o aplicativo atualizar o banco de dados.*/
        SQLiteDatabase db = getWritableDatabase();
        try {
            //Objeto de inserção de registros. Estrutura chave=>valor
            ContentValues values = new ContentValues();
            values.put("nome", astro.getNome());
            values.put("desc", astro.getDesc());
            values.put("tipo", astro.getTipo());
            if (id != 0) {
                String _id = String.valueOf(id);
                String[] whereArgs = new String[]{_id};
                /*##### ATUALIZANDO DADOS
                Equivalente ao SQL: update astro set nome="astro.getNome()",
                                                     desc="astro.getDesc()",
                                                     tipo="astro.getTipo()" where id=_id

                db.update(String tabela, ContentValues values, String where, String whereArgs[])

                tabela - Nome da tabela.

                values - Estrutura de chaves e valores.

                where - String com a cláusula where utilzada para identificar o registro. Caso
                fosse colocado id=1, o último argumento poderia ser nulo.

                whereArgs[] - Array com os parâmetros necessários, caso a cláusula where defina
                algum parâmetro com "?"
                 */
                int count = db.update("astro", values, "id=?", whereArgs);
                return count;
            } else {
                /*##### INSERINDO DADOS
                Equivalente ao SQL: insert into table astro(nome, desc, tipo) values
                                    ("astro.getNome()", "astro.getDesc()", "astro.getTipo()")

                db.insert(String tabela, String nullColumnHack, ContentValues values)

                tabela - Nome da tabela.

                nullColumnHack - Coluna que pode receber nulo caso "values" esteja vazio.

                values - estrutura de chaves e valores.
                 */
                id = db.insert("astro", "", values);
                return id;
            }
        } finally {
            //Fecha a conexão com o banco de dados
            db.close();
        }
    }
    // Deleta o Astro
    public int delete(Astro Astro) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            // Equivalente ao comando SQL: delete from astro where id=?
            int count = db.delete("astro", "id=?", new String[]{String.valueOf(Astro.getId())});
            Log.i(TAG, "Deletou [" + count + "] registro.");
            return count;
        } finally {
            db.close();
        }
    }

    // Deleta os Astros do tipo fornecido
    // Equivalente ao comando SQL: delete from astro where id=?
    public int deleteAstrosByTipo(String tipo) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            //Comando delete retorna o número de linhas apagadas
            int count = db.delete("astro", "tipo=?", new String[]{tipo});
            Log.i(TAG, "Deletou [" + count + "] registros");
            return count;
        } finally {
            db.close();
        }
    }

    // Consulta a lista com todos os Astros.
    // Equivalente ao comando SQL: select * from astro
    public List<Astro> findAll() {
        SQLiteDatabase db = getWritableDatabase();
        try {

            /*Método query(boolean distinct,
                           String tabela,
                           String colunas[],
                           String selecao[],
                           String selecaoArgs[],
                           String groupBy,
                           String orderBy)

            distinct - Garante que o resultado não contenha registros repetidos.

            String tabela - Nome da tabela

            colunas[] - Array com os nomes das colunas para seleção. Se array nulo, todas
            as colunas são retornadas.

            selecao[] - Contém a cláusula where utilizada para filtrar os registros. Se
            for informado um parâmetro nulo, todos os registros serão retornados.

            selecaoArgs[] - Argumentos "?" da cláusala where, caso necessário.

            groupBy - Nome das colunas para agrupar (group by).

            orderBy - Nome das colunas para ordenar (order by).
             */
            Cursor c = db.query("astro", null, null, null, null, null, null, null);
            return toList(c);
        } finally {
            db.close();
        }
    }

    // Consulta o Astro pelo tipo
    //Equivalente ao comando SQL: select id, nome, descricao, tipo from astro where tipo=?"
    public List<Astro> findAllByTipo(String tipo) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            Cursor c = db.query("astro", null, "tipo = '" + tipo + "'", null, null, null, null);
            Log.i(TAG, "Número de resultados:"+c.getCount());
            return toList(c);
        } finally {
            db.close();
        }
    }
    // Lê o cursor e cria a lista de Astros
    private List<Astro> toList(Cursor c) {
        List<Astro> astros = new ArrayList<Astro>();
        /* Posiciona o cursor para leitura no primeiro registro. Retorna true se algum registro é
        retornado pela consulta. */
        if (c.moveToFirst()) {
            do {
                Astro astro = new Astro();
                astros.add(astro);
                /* Recupera os atributos de Astro
                getColumnIndex(String) - Retorna o índice da coluna para o nome de coluna informado.
                O índice começa em zero e -1 é retornado se a coluna não existir
                 */
                astro.setId(c.getLong(c.getColumnIndex("id")));
                astro.setNome(c.getString(c.getColumnIndex("nome")));
                astro.setDesc(c.getString(c.getColumnIndex("desc")));
                astro.setTipo(c.getString(c.getColumnIndex("tipo")));
            /*Posiciona o cursor para leitura no próximo registro. Retorna true se existe mais um
            registro par ser lido. */
            } while (c.moveToNext());
        }
        return astros;
    }
    // Executa um SQL
    public void execSQL(String sql) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            db.execSQL(sql);
        } finally {
            db.close();
        }
    }
    // Executa um SQL
    public void execSQL(String sql, Object[] args) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            db.execSQL(sql, args);
        } finally {
            db.close();
        }
    }
}
