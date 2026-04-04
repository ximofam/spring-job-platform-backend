/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.htweb.pojo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author PC
 */
@Entity
@Table(name = "prod_tag")
@Getter
@Setter
@NamedQueries({
        @NamedQuery(name = "ProdTag.findAll", query = "SELECT p FROM ProdTag p"),
        @NamedQuery(name = "ProdTag.findById", query = "SELECT p FROM ProdTag p WHERE p.id = :id")})
public class ProdTag implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer id;
    @JoinColumn(name = "product_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private Product product;
    @JoinColumn(name = "tag_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private Tag tag;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProdTag)) {
            return false;
        }
        ProdTag other = (ProdTag) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ximofam.pojo.ProdTag[ id=" + id + " ]";
    }

}
