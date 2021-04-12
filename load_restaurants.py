import csv
import firebase_admin
from firebase_admin import credentials
from firebase_admin import firestore


FILE_NAME = "restaurant_list.csv"

cred = credentials.Certificate("app/serviceAccountKey.json")
default_app = firebase_admin.initialize_app(cred)
db = firebase_admin.firestore.client()

with open(FILE_NAME, "r") as f:
    csv_reader = csv.DictReader(f)
    for row in csv_reader:
        elem_id = row.pop("id")
        db.collection("restaurants").document(elem_id).update(row)

