package com.example.qe.model.query;


import java.util.List;

public abstract class CompositeQuery implements Query {

    List<Query> children;
}

