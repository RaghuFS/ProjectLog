package com.finstack.projectlog.entity;

import java.util.Date;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Base {

    private String createdUser;
    private String modifiedUser;
    private Date createdDate;
    private Date modifiedDate;
    private String authorisedUser;
    private Date authorisationDate;
    @Enumerated(EnumType.STRING)
    private TransactionStatusEnum transactionStatus;

    public void setModificationDetails(String username)
    {
        this.modifiedUser = username;
        this.modifiedDate = new Date();
        this.transactionStatus = TransactionStatusEnum.PENDING;
    }

    public void setCreationDetails(String username)
    {
        this.createdUser = username;
        this.createdDate = new Date();
        this.modifiedUser = username;
        this.modifiedDate = new Date();
        this.transactionStatus = TransactionStatusEnum.PENDING;
    }

    public void setAuthorizationDetails(String username)
    {
        this.authorisedUser = username;
        this.authorisationDate = new Date();
        this.transactionStatus = TransactionStatusEnum.MASTER;
    }
}