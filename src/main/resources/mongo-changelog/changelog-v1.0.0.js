// mongeez formatted javascript
// %%Ignore-License

// changeset nasahmed:US-15
db.groups.createIndex({ "name": 1 },{ unique: true ,collation: { locale : "en", strength : 2}});
