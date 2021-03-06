// mongeez formatted javascript
// %%Ignore-License

// changeset nasahmed:US-15
db.groups.dropIndex({ "name": 1 });
db.groups.createIndex({ "name": 1 },{ unique: true ,collation: { locale : "en", strength : 2}});

// changeset nasahmed:US-54
db.units.updateMany({"metricSystem": { $exists: true } }, { $rename: {"metricSystem": "measuringSystem" }});
db.units.updateMany({"measuringSystem":"MKS"}, { $set : {"measuringSystem": "METRIC" }});
db.units.updateMany({"measuringSystem":"MTS"}, { $set : {"measuringSystem": "METRIC" }});
db.units.updateMany({"measuringSystem":"CGS"}, { $set : {"measuringSystem": "IMPERIAL" }});

// changeset nasahmed:US-64
db.groups.updateMany({"baseUnitId": {$exists: false}}, { $set : {"baseUnitId":null}});
db.units.updateMany({"isBaseUnit":true}, { $set : {"isBaseUnit": false }});

//changeset nasahmed:US-74
db.units.updateMany({"formula": { $exists: true } }, { $rename: {"formula": "toBaseFormula" }});
db.units.updateMany({"fromBaseFormula": {$exists: false}}, { $set : {"fromBaseFormula":"[value]"}});
