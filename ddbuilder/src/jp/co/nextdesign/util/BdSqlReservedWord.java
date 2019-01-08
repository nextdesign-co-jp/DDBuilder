/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * Copyright(c) 2015 NEXT DESIGN Ltd.
 */
package jp.co.nextdesign.util;

import java.util.ArrayList;
import java.util.List;

/**
 * SQL予約語
 * @author murayama
 *
 */
public class BdSqlReservedWord {

	/**
	 * SQL言語の予約語であるか否か
	 * @param word
	 * @return
	 */
	public static boolean isReservedWord(String word){
		return word != null && SQL_RESERVED_WORD_LIST.contains(word.toUpperCase());
	}
	
	/** 重複のない予約語リスト */
	private static List<String> SQL_RESERVED_WORD_LIST;

	/** 主なRDBMSの予約語（重複あり） */
	private  static final String[] RDBMS_KEY_WORDS = {
		//--- PostgreSQL ---
		"A",
		"ABORT",
		"ABS",
		"ABSENT",
		"ABSOLUTE",
		"ACCESS",
		"ACCORDING",
		"ACTION",
		"ADA",
		"ADD",
		"ADMIN",
		"AFTER",
		"AGGREGATE",
		"ALIAS",
		"ALL",
		"ALLOCATE",
		"ALSO",
		"ALTER",
		"ALWAYS",
		"ANALYSE",
		"ANALYZE",
		"AND",
		"ANY",
		"ARE",
		"ARRAY",
		"ARRAY_AGG",
		"AS",
		"ASC",
		"ASENSITIVE",
		"ASSERTION",
		"ASSIGNMENT",
		"ASYMMETRIC",
		"AT",
		"ATOMIC",
		"ATTRIBUTE",
		"ATTRIBUTES",
		"AUTHORIZATION",
		"AVG",
		"BACKWARD",
		"BASE64",
		"BEFORE",
		"BEGIN",
		"BERNOULLI",
		"BETWEEN",
		"BIGINT",
		"BINARY",
		"BIT",
		"BITVAR",
		"BIT_LENGTH",
		"BLOB",
		"BOM",
		"BOOLEAN",
		"BOTH",
		"BREADTH",
		"BY",
		"C",
		"CACHE",
		"CALL",
		"CALLED",
		"CARDINALITY",
		"CASCADE",
		"CASCADED",
		"CASE",
		"CAST",
		"CATALOG",
		"CATALOG_NAME",
		"CEIL",
		"CEILING",
		"CHAIN",
		"CHAR",
		"CHARACTER",
		"CHARACTERISTICS",
		"CHARACTERS",
		"CHARACTER_LENGTH",
		"CHARACTER_SET_CATALOG",
		"CHARACTER_SET_NAME",
		"CHARACTER_SET_SCHEMA",
		"CHAR_LENGTH",
		"CHECK",
		"CHECKED",
		"CHECKPOINT",
		"CLASS",
		"CLASS_ORIGIN",
		"CLOB",
		"CLOSE",
		"CLUSTER",
		"COALESCE",
		"COBOL",
		"COLLATE",
		"COLLATION",
		"COLLATION_CATALOG",
		"COLLATION_NAME",
		"COLLATION_SCHEMA",
		"COLLECT",
		"COLUMN",
		"COLUMNS",
		"COLUMN_NAME",
		"COMMAND_FUNCTION",
		"COMMAND_FUNCTION_CODE",
		"COMMENT",
		"COMMIT",
		"COMMITTED",
		"COMPLETION",
		"CONCURRENTLY",
		"CONDITION",
		"CONDITION_NUMBER",
		"CONFIGURATION",
		"CONNECT",
		"CONNECTION",
		"CONNECTION_NAME",
		"CONSTRAINT",
		"CONSTRAINTS",
		"CONSTRAINT_CATALOG",
		"CONSTRAINT_NAME",
		"CONSTRAINT_SCHEMA",
		"CONSTRUCTOR",
		"CONTAINS",
		"CONTENT",
		"CONTINUE",
		"CONVERSION",
		"CONVERT",
		"COPY",
		"CORR",
		"CORRESPONDING",
		"COST",
		"COUNT",
		"COVAR_POP",
		"COVAR_SAMP",
		"CREATE",
		"CREATEDB",
		"CREATEROLE",
		"CREATEUSER",
		"CROSS",
		"CSV",
		"CUBE",
		"CUME_DIST",
		"CURRENT",
		"CURRENT_CATALOG",
		"CURRENT_DATE",
		"CURRENT_DEFAULT_TRANSFORM_GROUP",
		"CURRENT_PATH",
		"CURRENT_ROLE",
		"CURRENT_SCHEMA",
		"CURRENT_TIME",
		"CURRENT_TIMESTAMP",
		"CURRENT_TRANSFORM_GROUP_FOR_TYPE",
		"CURRENT_USER",
		"CURSOR",
		"CURSOR_NAME",
		"CYCLE",
		"DATA",
		"DATABASE",
		"DATE",
		"DATETIME_INTERVAL_CODE",
		"DATETIME_INTERVAL_PRECISION",
		"DAY",
		"DEALLOCATE",
		"DEC",
		"DECIMAL",
		"DECLARE",
		"DEFAULT",
		"DEFAULTS",
		"DEFERRABLE",
		"DEFERRED",
		"DEFINED",
		"DEFINER",
		"DEGREE",
		"DELETE",
		"DELIMITER",
		"DELIMITERS",
		"DENSE_RANK",
		"DEPTH",
		"DEREF",
		"DERIVED",
		"DESC",
		"DESCRIBE",
		"DESCRIPTOR",
		"DESTROY",
		"DESTRUCTOR",
		"DETERMINISTIC",
		"DIAGNOSTICS",
		"DICTIONARY",
		"DISABLE",
		"DISCARD",
		"DISCONNECT",
		"DISPATCH",
		"DISTINCT",
		"DO",
		"DOCUMENT",
		"DOMAIN",
		"DOUBLE",
		"DROP",
		"DYNAMIC",
		"DYNAMIC_FUNCTION",
		"DYNAMIC_FUNCTION_CODE",
		"EACH",
		"ELEMENT",
		"ELSE",
		"EMPTY",
		"ENABLE",
		"ENCODING",
		"ENCRYPTED",
		"END",
		"END-EXEC",
		"ENUM",
		"EQUALS",
		"ESCAPE",
		"EVERY",
		"EXCEPT",
		"EXCEPTION",
		"EXCLUDE",
		"EXCLUDING",
		"EXCLUSIVE",
		"EXEC",
		"EXECUTE",
		"EXISTING",
		"EXISTS",
		"EXP",
		"EXPLAIN",
		"EXTERNAL",
		"EXTRACT",
		"FALSE",
		"FAMILY",
		"FETCH",
		"FILTER",
		"FINAL",
		"FIRST",
		"FIRST_VALUE",
		"FLAG",
		"FLOAT",
		"FLOOR",
		"FOLLOWING",
		"FOR",
		"FORCE",
		"FOREIGN",
		"FORTRAN",
		"FORWARD",
		"FOUND",
		"FREE",
		"FREEZE",
		"FROM",
		"FULL",
		"FUNCTION",
		"FUSION",
		"G",
		"GENERAL",
		"GENERATED",
		"GET",
		"GLOBAL",
		"GO",
		"GOTO",
		"GRANT",
		"GRANTED",
		"GREATEST",
		"GROUP",
		"GROUPING",
		"HANDLER",
		"HAVING",
		"HEADER",
		"HEX",
		"HIERARCHY",
		"HOLD",
		"HOST",
		"HOUR",
		"ID",
		"IDENTITY",
		"IF",
		"IGNORE",
		"ILIKE",
		"IMMEDIATE",
		"IMMUTABLE",
		"IMPLEMENTATION",
		"IMPLICIT",
		"IN",
		"INCLUDING",
		"INCREMENT",
		"INDENT",
		"INDEX",
		"INDEXES",
		"INDICATOR",
		"INFIX",
		"INHERIT",
		"INHERITS",
		"INITIALIZE",
		"INITIALLY",
		"INNER",
		"INOUT",
		"INPUT",
		"INSENSITIVE",
		"INSERT",
		"INSTANCE",
		"INSTANTIABLE",
		"INSTEAD",
		"INT",
		"INTEGER",
		"INTERSECT",
		"INTERSECTION",
		"INTERVAL",
		"INTO",
		"INVOKER",
		"IS",
		"ISNULL",
		"ISOLATION",
		"ITERATE",
		"JOIN",
		"K",
		"KEY",
		"KEY_MEMBER",
		"KEY_TYPE",
		"LAG",
		"LANCOMPILER",
		"LANGUAGE",
		"LARGE",
		"LAST",
		"LAST_VALUE",
		"LATERAL",
		"LC_COLLATE",
		"LC_CTYPE",
		"LEAD",
		"LEADING",
		"LEAST",
		"LEFT",
		"LENGTH",
		"LESS",
		"LEVEL",
		"LIKE",
		"LIKE_REGEX",
		"LIMIT",
		"LISTEN",
		"LN",
		"LOAD",
		"LOCAL",
		"LOCALTIME",
		"LOCALTIMESTAMP",
		"LOCATION",
		"LOCATOR",
		"LOCK",
		"LOGIN",
		"LOWER",
		"M",
		"MAP",
		"MAPPING",
		"MATCH",
		"MATCHED",
		"MAX",
		"MAXVALUE",
		"MAX_CARDINALITY",
		"MEMBER",
		"MERGE",
		"MESSAGE_LENGTH",
		"MESSAGE_OCTET_LENGTH",
		"MESSAGE_TEXT",
		"METHOD",
		"MIN",
		"MINUTE",
		"MINVALUE",
		"MOD",
		"MODE",
		"MODIFIES",
		"MODIFY",
		"MODULE",
		"MONTH",
		"MORE",
		"MOVE",
		"MULTISET",
		"MUMPS",
		"NAME",
		"NAMES",
		"NAMESPACE",
		"NATIONAL",
		"NATURAL",
		"NCHAR",
		"NCLOB",
		"NESTING",
		"NEW",
		"NEXT",
		"NFC",
		"NFD",
		"NFKC",
		"NFKD",
		"NIL",
		"NO",
		"NOCREATEDB",
		"NOCREATEROLE",
		"NOCREATEUSER",
		"NOINHERIT",
		"NOLOGIN",
		"NONE",
		"NORMALIZE",
		"NORMALIZED",
		"NOSUPERUSER",
		"NOT",
		"NOTHING",
		"NOTIFY",
		"NOTNULL",
		"NOWAIT",
		"NTH_VALUE",
		"NTILE",
		"NULL",
		"NULLABLE",
		"NULLIF",
		"NULLS",
		"NUMBER",
		"NUMERIC",
		"OBJECT",
		"OCCURRENCES_REGEX",
		"OCTETS",
		"OCTET_LENGTH",
		"OF",
		"OFF",
		"OFFSET",
		"OIDS",
		"OLD",
		"ON",
		"ONLY",
		"OPEN",
		"OPERATION",
		"OPERATOR",
		"OPTION",
		"OPTIONS",
		"OR",
		"ORDER",
		"ORDERING",
		"ORDINALITY",
		"OTHERS",
		"OUT",
		"OUTER",
		"OUTPUT",
		"OVER",
		"OVERLAPS",
		"OVERLAY",
		"OVERRIDING",
		"OWNED",
		"OWNER",
		"P",
		"PAD",
		"PARAMETER",
		"PARAMETERS",
		"PARAMETER_MODE",
		"PARAMETER_NAME",
		"PARAMETER_ORDINAL_POSITION",
		"PARAMETER_SPECIFIC_CATALOG",
		"PARAMETER_SPECIFIC_NAME",
		"PARAMETER_SPECIFIC_SCHEMA",
		"PARSER",
		"PARTIAL",
		"PARTITION",
		"PASCAL",
		"PASSING",
		"PASSWORD",
		"PATH",
		"PERCENTILE_CONT",
		"PERCENTILE_DISC",
		"PERCENT_RANK",
		"PLACING",
		"PLANS",
		"PLI",
		"POSITION",
		"POSITION_REGEX",
		"POSTFIX",
		"POWER",
		"PRECEDING",
		"PRECISION",
		"PREFIX",
		"PREORDER",
		"PREPARE",
		"PREPARED",
		"PRESERVE",
		"PRIMARY",
		"PRIOR",
		"PRIVILEGES",
		"PROCEDURAL",
		"PROCEDURE",
		"PUBLIC",
		"QUOTE",
		"RANGE",
		"RANK",
		"READ",
		"READS",
		"REAL",
		"REASSIGN",
		"RECHECK",
		"RECURSIVE",
		"REF",
		"REFERENCES",
		"REFERENCING",
		"REGR_AVGX",
		"REGR_AVGY",
		"REGR_COUNT",
		"REGR_INTERCEPT",
		"REGR_R2",
		"REGR_SLOPE",
		"REGR_SXX",
		"REGR_SXY",
		"REGR_SYY",
		"REINDEX",
		"RELATIVE",
		"RELEASE",
		"RENAME",
		"REPEATABLE",
		"REPLACE",
		"REPLICA",
		"RESET",
		"RESPECT",
		"RESTART",
		"RESTRICT",
		"RESULT",
		"RETURN",
		"RETURNED_CARDINALITY",
		"RETURNED_LENGTH",
		"RETURNED_OCTET_LENGTH",
		"RETURNED_SQLSTATE",
		"RETURNING",
		"RETURNS",
		"REVOKE",
		"RIGHT",
		"ROLE",
		"ROLLBACK",
		"ROLLUP",
		"ROUTINE",
		"ROUTINE_CATALOG",
		"ROUTINE_NAME",
		"ROUTINE_SCHEMA",
		"ROW",
		"ROWS",
		"ROW_COUNT",
		"ROW_NUMBER",
		"RULE",
		"SAVEPOINT",
		"SCALE",
		"SCHEMA",
		"SCHEMA_NAME",
		"SCOPE",
		"SCOPE_CATALOG",
		"SCOPE_NAME",
		"SCOPE_SCHEMA",
		"SCROLL",
		"SEARCH",
		"SECOND",
		"SECTION",
		"SECURITY",
		"SELECT",
		"SELF",
		"SENSITIVE",
		"SEQUENCE",
		"SERIALIZABLE",
		"SERVER",
		"SERVER_NAME",
		"SESSION",
		"SESSION_USER",
		"SET",
		"SETOF",
		"SETS",
		"SHARE",
		"SHOW",
		"SIMILAR",
		"SIMPLE",
		"SIZE",
		"SMALLINT",
		"SOME",
		"SOURCE",
		"SPACE",
		"SPECIFIC",
		"SPECIFICTYPE",
		"SPECIFIC_NAME",
		"SQL",
		"SQLCODE",
		"SQLERROR",
		"SQLEXCEPTION",
		"SQLSTATE",
		"SQLWARNING",
		"SQRT",
		"STABLE",
		"STANDALONE",
		"START",
		"STATE",
		"STATEMENT",
		"STATIC",
		"STATISTICS",
		"STDDEV_POP",
		"STDDEV_SAMP",
		"STDIN",
		"STDOUT",
		"STORAGE",
		"STRICT",
		"STRIP",
		"STRUCTURE",
		"STYLE",
		"SUBCLASS_ORIGIN",
		"SUBLIST",
		"SUBMULTISET",
		"SUBSTRING",
		"SUBSTRING_REGEX",
		"SUM",
		"SUPERUSER",
		"SYMMETRIC",
		"SYSID",
		"SYSTEM",
		"SYSTEM_USER",
		"T",
		"TABLE",
		"TABLESAMPLE",
		"TABLESPACE",
		"TABLE_NAME",
		"TEMP",
		"TEMPLATE",
		"TEMPORARY",
		"TERMINATE",
		"TEXT",
		"THAN",
		"THEN",
		"TIES",
		"TIME",
		"TIMESTAMP",
		"TIMEZONE_HOUR",
		"TIMEZONE_MINUTE",
		"TO",
		"TOP_LEVEL_COUNT",
		"TRAILING",
		"TRANSACTION",
		"TRANSACTIONS_COMMITTED",
		"TRANSACTIONS_ROLLED_BACK",
		"TRANSACTION_ACTIVE",
		"TRANSFORM",
		"TRANSFORMS",
		"TRANSLATE",
		"TRANSLATE_REGEX",
		"TRANSLATION",
		"TREAT",
		"TRIGGER",
		"TRIGGER_CATALOG",
		"TRIGGER_NAME",
		"TRIGGER_SCHEMA",
		"TRIM",
		"TRIM_ARRAY",
		"TRUE",
		"TRUNCATE",
		"TRUSTED",
		"TYPE",
		"UESCAPE",
		"UNBOUNDED",
		"UNCOMMITTED",
		"UNDER",
		"UNENCRYPTED",
		"UNION",
		"UNIQUE",
		"UNKNOWN",
		"UNLISTEN",
		"UNNAMED",
		"UNNEST",
		"UNTIL",
		"UNTYPED",
		"UPDATE",
		"UPPER",
		"URI",
		"USAGE",
		"USER",
		"USER_DEFINED_TYPE_CATALOG",
		"USER_DEFINED_TYPE_CODE",
		"USER_DEFINED_TYPE_NAME",
		"USER_DEFINED_TYPE_SCHEMA",
		"USING",
		"VACUUM",
		"VALID",
		"VALIDATOR",
		"VALUE",
		"VALUES",
		"VARBINARY",
		"VARCHAR",
		"VARIABLE",
		"VARIADIC",
		"VARYING",
		"VAR_POP",
		"VAR_SAMP",
		"VERBOSE",
		"VERSION",
		"VIEW",
		"VOLATILE",
		"WHEN",
		"WHENEVER",
		"WHERE",
		"WHITESPACE",
		"WIDTH_BUCKET",
		"WINDOW",
		"WITH",
		"WITHIN",
		"WITHOUT",
		"WORK",
		"WRAPPER",
		"WRITE",
		"XML",
		"XMLAGG",
		"XMLATTRIBUTES",
		"XMLBINARY",
		"XMLCAST",
		"XMLCOMMENT",
		"XMLCONCAT",
		"XMLDECLARATION",
		"XMLDOCUMENT",
		"XMLELEMENT",
		"XMLEXISTS",
		"XMLFOREST",
		"XMLITERATE",
		"XMLNAMESPACES",
		"XMLPARSE",
		"XMLPI",
		"XMLQUERY",
		"XMLROOT",
		"XMLSCHEMA",
		"XMLSERIALIZE",
		"XMLTABLE",
		"XMLTEXT",
		"XMLVALIDATE",
		"YEAR",
		"YES",
		"ZONE",
		//--- ORACLE ---
		"ADD",
		"ALL",
		"ALTER",
		"AND",
		"ANY",
		"AS",
		"ASC",
		"AUDIT",
		"BETWEEN",
		"BY",
		"CHAR",
		"CHECK",
		"CLUSTER",
		"COLUMN",
		"COLUMN_VALUE",
		"COMMENT",
		"COMPRESS",
		"CONNECT",
		"CREATE",
		"CURRENT",
		"DATE",
		"DECIMAL",
		"DEFAULT",
		"DELETE",
		"DESC",
		"DISTINCT",
		"DROP",
		"ELSE",
		"EXCLUSIVE",
		"EXISTS",
		"FILE",
		"FLOAT",
		"FOR",
		"FROM",
		"GRANT",
		"GROUP",
		"HAVING",
		"IDENTIFIED",
		"IMMEDIATE",
		"IN",
		"INCREMENT",
		"INDEX",
		"INITIAL",
		"INSERT",
		"INTEGER",
		"INTERSECT",
		"INTO",
		"IS",
		"LEVEL",
		"LIKE",
		"LOCK",
		"LONG",
		"MAXEXTENTS",
		"MINUS",
		"MLSLABEL",
		"MODE",
		"MODIFY",
		"NESTED_TABLE_ID",
		"NOAUDIT",
		"NOCOMPRESS",
		"NOT",
		"NOWAIT",
		"NULL",
		"NUMBER",
		"OF",
		"OFFLINE",
		"ON",
		"ONLINE",
		"OPTION",
		"OR",
		"ORDER",
		"PCTFREE",
		"PRIOR",
		"PRIVILEGES",
		"PUBLIC",
		"RAW",
		"RENAME",
		"RESOURCE",
		"REVOKE",
		"ROW",
		"ROWID",
		"ROWNUM",
		"ROWS",
		"SELECT",
		"SESSION",
		"SET",
		"SHARE",
		"SIZE",
		"SMALLINT",
		"START",
		"SUCCESSFUL",
		"SYNONYM",
		"SYSDATE",
		"TABLE",
		"THEN",
		"TO",
		"TRIGGER",
		"UID",
		"UNION",
		"UNIQUE",
		"UPDATE",
		"USER",
		"VALIDATE",
		"VALUES",
		"VARCHAR",
		"VARCHAR2",
		"VIEW",
		"WHENEVER",
		"WHERE",
		"WITH",
		//--- SQL Server ---
		"ACCESS",
		"ADD",
		"ALL",
		"ALTER",
		"AND",
		"ANY",
		"AS",
		"ASC",
		"AUTHORIZATION",
		"AVG",
		"BACKUP",
		"BEGIN",
		"BETWEEN",
		"BREAK",
		"BROWSE",
		"BULK",
		"BY",
		"CASCADE",
		"CASE",
		"CHECK",
		"CHECKPOINT",
		"CLOSE",
		"CLUSTERED",
		"COALESCE",
		"COLLATE",
		"COLUMN",
		"COMMIT",
		"COMPUTE",
		"CONSTRAINT",
		"CONTAINS",
		"CONTAINSTABLE",
		"CONTINUE",
		"CONVERT",
		"COUNT",
		"CREATE",
		"CROSS",
		"CURRENT",
		"CURRENT_DATE",
		"CURRENT_TIME",
		"CURRENT_TIMESTAMP",
		"CURRENT_USER",
		"CURSOR",
		"DATABASE",
		"DATABASEPASSWORD",
		"DATEADD",
		"DATEDIFF",
		"DATENAME",
		"DATEPART",
		"DBCC",
		"DEALLOCATE",
		"DECLARE",
		"DEFAULT",
		"DELETE",
		"DENY",
		"DESC",
		"DISK",
		"DISTINCT",
		"DISTRIBUTED",
		"DOUBLE",
		"DROP",
		"DUMP",
		"ELSE",
		"ENCRYPTION",
		"END",
		"ERRLVL",
		"ESCAPE",
		"EXCEPT",
		"EXEC",
		"EXECUTE",
		"EXISTS",
		"EXIT",
		"EXPRESSION",
		"FETCH",
		"FILE",
		"FILLFACTOR",
		"FOR",
		"FOREIGN",
		"FREETEXT",
		"FREETEXTTABLE",
		"FROM",
		"FULL",
		"FUNCTION",
		"GOTO",
		"GRANT",
		"GROUP",
		"HAVING",
		"HOLDLOCK",
		"IDENTITY",
		"IDENTITY",
		"IDENTITY_INSERT",
		"IDENTITYCOL",
		"IF",
		"IN",
		"INDEX",
		"INNER",
		"INSERT",
		"INTERSECT",
		"INTO",
		"IS",
		"JOIN",
		"KEY",
		"KILL",
		"LEFT",
		"LIKE",
		"LINENO",
		"LOAD",
		"MAX",
		"MIN",
		"NATIONAL",
		"NOCHECK",
		"NONCLUSTERED",
		"NOT",
		"NULL",
		"NULLIF",
		"OF",
		"OFF",
		"OFFSETS",
		"ON",
		"OPEN",
		"OPENDATASOURCE",
		"OPENQUERY",
		"OPENROWSET",
		"OPENXML",
		"OPTION",
		"OR",
		"ORDER",
		"OUTER",
		"OVER",
		"PERCENT",
		"PLAN",
		"PRECISION",
		"PRIMARY",
		"PRINT",
		"PROC",
		"PROCEDURE",
		"PUBLIC",
		"RAISERROR",
		"READ",
		"READTEXT",
		"RECONFIGURE",
		"REFERENCES",
		"REPLICATION",
		"RESTORE",
		"RESTRICT",
		"RETURN",
		"REVOKE",
		"RIGHT",
		"ROLLBACK",
		"ROWCOUNT",
		"ROWGUIDCOL",
		"RULE",
		"SAVE",
		"SCHEMA",
		"SELECT",
		"SESSION_USER",
		"SET",
		"SETUSER",
		"SHUTDOWN",
		"SOME",
		"STATISTICS",
		"SUM",
		"SYSTEM_USER",
		"TABLE",
		"TEXTSIZE",
		"THEN",
		"TO",
		"TOP",
		"TRAN",
		"TRANSACTION",
		"TRIGGER",
		"TRUNCATE",
		"TSEQUAL",
		"UNION",
		"UNIQUE",
		"UPDATE",
		"UPDATETEXT",
		"USE",
		"USER",
		"VALUES",
		"VARYING",
		"VIEW",
		"WAITFOR",
		"WHEN",
		"WHERE",
		"WHILE",
		"WITH",
		"WRITETEXT"
		};

	/** 静的コンストラクタ */
	static {
		SQL_RESERVED_WORD_LIST = new ArrayList<String>();
		for(String word : RDBMS_KEY_WORDS){
			if (word != null && !word.isEmpty() && !SQL_RESERVED_WORD_LIST.contains(word)){
				SQL_RESERVED_WORD_LIST.add(word);
			}
		}
	}
}
