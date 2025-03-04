db.createUser({
    user: "mongo",
    pwd: "mongo",
    roles: [{
        role: "readWrite",
        db: "dictionary"
    }]
});
db.createCollection("dictionary");