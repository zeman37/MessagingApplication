/*
 * This file is generated by jOOQ.
 */
package com.application.generated.db;


import com.application.generated.db.tables.Messages;
import com.application.generated.db.tables.Users;
import com.application.generated.db.tables.records.MessagesRecord;
import com.application.generated.db.tables.records.UsersRecord;

import org.jooq.ForeignKey;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;


/**
 * A class modelling foreign key relationships and constraints of tables in the
 * default schema.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Keys {

    // -------------------------------------------------------------------------
    // UNIQUE and PRIMARY KEY definitions
    // -------------------------------------------------------------------------

    public static final UniqueKey<MessagesRecord> PK_MESSAGES = Internal.createUniqueKey(Messages.MESSAGES, DSL.name("PK_MESSAGES"), new TableField[] { Messages.MESSAGES.ID }, true);
    public static final UniqueKey<UsersRecord> PK_USERS = Internal.createUniqueKey(Users.USERS, DSL.name("PK_USERS"), new TableField[] { Users.USERS.ID }, true);

    // -------------------------------------------------------------------------
    // FOREIGN KEY definitions
    // -------------------------------------------------------------------------

    public static final ForeignKey<MessagesRecord, UsersRecord> FK_MESSAGES_USERS = Internal.createForeignKey(Messages.MESSAGES, DSL.name("FK_MESSAGES_USERS"), new TableField[] { Messages.MESSAGES.USER_ID }, Keys.PK_USERS, new TableField[] { Users.USERS.ID }, true);
}
