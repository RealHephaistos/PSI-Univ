# Introduction

## Context

Actuellement en licence 3 informatique à l'université de Rennes 1 nous consacrons beaucoup de temps à
rechercher des salles (essentiellement à l'ISTIC qui est notre lieu de travail principal). Pendant
notre temps libre et dans le but de travailler, nous recherchons des salles à l'aide de l'emploi du temps qui nous est fournis.
Cependant, l'ergonomie de cet outil pour trouver des salles n'étant pas la principale priorité de
l'application ne permet pas une recherche simple et efficace. En effet, il faut vérifier l'emploi de chaque salle afin de déterminer si elles sont disponibles ou non.

## Problematique

Nous nous sommes alors posé la question de savoir s'il n'existerait pas un moyen plus simple de
chercher et vérifier qu'une salle est disponible ou non ?

## Solution

Nous avons donc décidé de créer une application android qui permettrait aux étudiants de pouvoir chercher
ou de voir à l'aide d'une carte interactive qu'elles sont les salles disponibles à un moment précis. Pour cela nous avons pensé à utiliser les
données de l'outil d'emploi du temps qui nous est fournis.

# Description du projet

Application android qui affiche les salles disponibles sur un plan du campus en se basant sur le
planning de la fac. Cette application permettrait aux élèves de trouver plus rapidement quelles
salles sont disponibles et dans quels bâtiments.

## Cahier des charges

- Réaliser une application mobile Android dans un délai de 6 semaines qui nous a été donné.
- Réaliser une application mobile Android fonctionnant sur un grand nombre d'appareils.
- Stockage les données nécessaires au fonctionnement de l'application dans une base de données.
- Réaliser une carte manipulable et interactive : zoom, déplacement et click sur des zones précises (représentant les
  bâtiments que nous allons traiter, en l'occurrence l'ISTIC). L'interaction avec cette carte devra afficher les plans de bâtiments avec les salles. Chaque salle pouvant être utilisée par les étudiants devra être cliquable et affichera alors si la salle est disponible ou non. De plus, le prochain cours qui se déroulera dans cette salle sera affichée afin de pouvoir déterminer la durée pendant laquelle la salle sera à disposition si elle est disponible.
- Réaliser le bâtiment de l'ISTIC complètement si possible (avec la possibilité de pouvoir étendre
  l'application à l'ensemble de l'université de Rennes 1 et d'autres universités)
- Réaliser une barre de recherche permettant de rechercher une salle précise (dans les bâtiments
  traités)
- Réaliser un menu permettant d'accéder à la page d'accueil, recherche avancée et de paramètres
- Réaliser un système de recherche avancé avec le choix de pouvoir choisir une date et une heure
  précise
- Réaliser une page de paramètres permettant de changer la langue de l'application (au moins 2), le
  format de la date

## Répartition des tâches

Séparation du groupe de 4 en 2 groupes de 2 personnes

- 2 personnes sur la partie Backend, elle-même séparé en 2 groupes individuels
    - Carte : Berrouche Issameddine
    - Navigation / Menu : Buan Tony
- 2 personnes sur la partie Frontend
    - Traitement des données et de la base de données côté serveur : Hamono Morvan
    - Récuperation des données et base de données côté client : Vachez Guillaume

## Outils utilisés (+raisons)

Utilisation de [Discord](https://discord.com/) pour pouvoir communiquer entre tous les membres du groupe
et de notre professeur tuteur à l'aide du chat vocal pendant les réunions. Avoir une trace écrite des idées / problèmes
rencontrés avec le système de message. Nous n'avons pas choisi une autre plateforme car discord est une plateforme
que tous les membres du groupe connaissaient et utilisaient régulièrement contrairement à d'autres
plateforme. 

Utilisation d'[Android Studio](https://developer.android.com/studio) afin de réaliser le projet car
il s'agit d'un des environnements de développement les plus connus et utilisé pour réaliser des applications mobiles Android.Une documentation importante et de nombreuses librairies sur internet qui nous ont permis d'avoir plus
d'outils à disposition pour répondre à nos questions et aux problèmes rencontrés ou encore de ne pas écrire du code
trop complexe qui nous prendrait trop de temps pour un projet de 6 semaines. Sachant que les deux
seuls langages de programmation disponibles dans Android Studio sont java et kotlyn, nous avons
décidé d'utiliser java. En effet, il s'agit d'un language de programmation que nous avons étudié ces
trois dernières années. Nous disposons donc de base solide avec ce langage de programmation.

Utilisation de [Github](https://github.com/) qui a permis de partager le code entre toutes les
personnes du groupe ainsi que les professeurs. Ceci nous à aussi permis de travailler simultanément sur le projet sans devoir attendre que les personnes aient fini d'écrire leur code. Nous savions déjà utiliser la plateforme suite aux cours que nous avions eu sur
Gitlab (GEN). Nous avons cependant préféré utiliser GitHub afin de garder une trace de notre travail
après nos études (ne sachant pas mais supposant que nos comtes Gitlab allaient être supprimés). De
plus, une extension était disponible dans Android Studio ce qui nous a permis de fair le lien entre
les deux très facilement. Enfin, un système de hiérarchie très pratique grâce aux commits qui nous ont
permis de retourner sur d'ancienne version en cas de problème avec le code actuel.

- Berrouche Issameddine (RealHephaistos)
- Buan Tony (tbuan, NoctTB)
    - tbuan (commits réalisé depuis Android Studio) ->  N'est pas un compte GithHub donc les commits
      n'apparaissent pas dans les statistiques
- Hamono Morvan (MorvanH)
- Vachez Guillaume (xelphire)

Utilisation de [SQLite](https://www.sqlite.org/index.html) afin de pouvoir stocker les données
nécessaires pour le bon fonctionnement de l'application, c'est-à-dire les données de l'emploi du
temps. Nous avons décidé d'utiliser SQLite car nous savions déjà utlisé mySQL (DSB) et nous voulions
pouvoir garder notre travail pour le futur. Nous n'avions pas besoin d'une table de données
composés d'un nombre de données très importantes donc nous avons favorisé SQLite a SQL.

Utilisation d'[Azure](https://azure.microsoft.com/fr-fr/) afin de stocker la base de données en ligne
et de ne pas avoir besoin de la stocker sur l'application et donc réduire la taille de celle-ci. Nous
avons utilisé Azure car cela est géré par Microsoft et qu'un compte gratuit était disponible car
nous sommes étudiants. De plus des caractéristiques largement suffisantes pour notre base de données (débit,...).

## Mise en Oeuvre

- Carnet de Bord

# Gestion du projet

## BackEnd

### Debut du project
Notre première étape à été de ce mettre d'accord sur la forme que prendrait nos donnée pour les 
manipuler de la même façon tout en travaillant séparement, tout cela en prévision de la mise en 
commun de chacune de nos partie. Nous avons donc rapidement mis au point differentes  classes 
representants un bâtiment, un étage, une salle et un cours. Toutes ces classes étant liée : un 
bâtiment contient des étages, un étages des salles et une salles des cours. Afin de facilité 
l'implementation, chaque classe a des information sous forme de String comme par exemple le nom et 
chaque objet ce souvient des informations utiles de son parent. Ainsi une salle connait le nom de 
son étage et de son bâtiment.

### Récupération des ICS

### Traitement des informations
Les fichiers en .ics étant que du texte avec des mots clefs suivi de valeurs comme par exemple 
"SUBJECT:Maths" pour exprimer que l'evenement est un cours de maths,nous avons du les transformer en
un format utilisable. Pour cela nous avons essayer d'utiliser des librairies déjà existante mais aucune
ne semblait faire de l'extraction de donnée comme nous le voulions. Nous avons donc fait un "parser"
nous même qui extrait les données en forme de String. Nous n'avions plus qu'à faire utiliser ces 
Strings pour créer des objets Java comme des salles ou des evenements. Afin que tous les clients aient
accès aux données et pour que le serveur des emplois du temps ne soit pas surcharger, nous avons
décider de récuperer les données et de faire ce traitement qu'une seule fois qui envoie les données 
sur un serveur mySql oû tous les appareils pourraient ce connecté.

### Base de donnée
La base de donnée a été élaborée grâce à un serveur wamp privé et local afin de faire des tests avant
de passer sur Azure pour avoir une base de donnée commune et externe pour travaillé sur le projet 
dans une situation réelle. A terme, si l'application devait être "produite", nous aurions un serveur 
physique privé capable d'héberger ce serveur mySql et d'effectuer les mises à jours régulierement.

- (Image)Schema bdd


### Base de donnée locale

## FrontEnd

### Barre de recherche

- (Image)Barre Recherche 
- (Image)Barre de recherche avancé

Afin de satisfaire le cahier des charges, l'utlisateur doit pouvoir chercher une salle précise de
façon simple et efficace. Pour cela nous avons utlisé l'outil d'Android Studio "SearchView" qui nous
a permis d'implenter la fonction de recherche. Cette barre de recheche ne fonctionne qu'avec le clic
sur la salle dans le menu deroulant (identique dans les deux barres de recherche). 

...

Les deux barres de recherche possède une icone de menu qui permet d'ouvrir un menu tiroir et de navigué entres toutes les activités possibles de l'application.

### Menu

Le menu est séparé en deux parties. 

- (Image)Menu
- Le menu tiroir qui s'affiche lorsque l'on appuie sur l'icone du menu situé en haut des différentes pages. Celui-ci ci peut aussi être ouvert en réalisant un scroll de gauche à droite. Différentes options apparaissent tout d'abord l'option acceuil permettant de revenir à la page contenant le plan du batiments. Le(s) option(s) dans la catégorie lieux ne sont pas utilisé car notre application n'est centré que sur l'université de Rennes 1 mais ces otions pourraient être utilisé pour ajouté d'autes facultés. Enfin l'option paramètres permettant d'ouvrir une page de paramètre. Toutes les options sauf les options de la catégorie lieux sont clickables et permmettent la nivigation entre tous les éléments de l'application
- (Image)ToolBar AdvancedSearch
- (Image)ToolBar Settings
- Les barres d'outils disponibles sur toutes les activités saauf celle d'accueil qui permettent un retout en arrière ainsi que l'ouverture du menu tiroit


### Parametres

### Recherche avancée

### Vue de la carte du campus

Afin de satisfaire le cahier des charges, l'utilisateur doit pouvoir zoomer et déplacer la carte du
campus (idéalement avec un défilement fluide), ainsi que cliquer sur les bâtiments. La carte du
campus étant trop grande et trop complexe pour etre stocké sous format SVG, nous n'avons pas pu
utiliser la même méthode que pour la vue des bâtiments.

Pour le déplacement de la carte, Android Studio n'offrant pas de solutions natives permettant de
gérer le déplacement sur des images, nous avions initialement pensé à programmer nous-mêmes un
composant permettant de zoomer er de scroller dans deux dimmensions. Mais après une version bêta
techniquement fonctionnelle, bien que sans mouvement fluide, nous nous sommes rendu compte qu'il
serait plus facile d'implémenter la librarie [PhotoView](https://github.com/Baseflow/PhotoView).

Pour rendre certaines parties de la cartes intéractives, nous avons
utilisé [un outil permettant de créer des imagemap](https://www.image-map.net/). Ce site permet de
tracer des formes sur une image et génère automatiquement un fichier XML contenant les coordonnés des
points du polygone. La classe MapPhotoView (notre implémentation de PhotoView) utilise ce fichier
pour créer une liste de polygones. Quand le composant MapPhotoView de l'activité principale détecte
un click, il parcour la liste des polygones pour vérifier si l'utilisateur a cliqué sur un
batiment, et ouvre la vue bâtiment correspondante si c'est le cas.

### Vue des batiments

## Partie commune

## Validation de l'implémentation

# Conclusion

## Issam

## Tony

Un projet que j'ai fortement apprécié réalisé. Un projet libre sur un sujet qui me plaisait et dont
les objectifs finaux ont été réalisé. N'étant pas spécialement adepte de la création de code j'ai
beacoup aimé réalisé l'interface graphique de l'application grâce aux outils fournis par Android
Studio. De nombreux petits détails qui posent souvent problèmes mais que j'ai trouvé satisfaisant à
résoudre. Un projet totalement libre qui a cependant quelques contraintes. En effet je trouve que
l'on peut perdre facilement la notion du temps de travail, ne pas savoir si on a travaillé assez,
cependant ces doutes sont souvent remis en cause lors des réunions avec le professeur tuteur ce qui
je trouve est une bonne chose. Enfin, comme dans tous projets ils est difficile de savoir par ou
commencé car contrairement aux projets réalisés en cours nous ne sommes pas guidé.

## Morvan
Le fait de pouvoir choisir le thème, la techno et les gens avec qui ont travail procure un vrai 
sentiment de liberté et permet de ce travaillé à fond dans quelque chose qui nous intéresse et nous
appartient. Je pense d'ailleur que cela nous a permis de travaillé plus rapidement efficacement.
Le fait de devoir apprendre un langage ou un logiciel est déroutant au début car on se dit que l'on 
prends beaucoup de temps sans vraiment produire mais cela permet d'être en situation réelle. Sur un
projet profesionnel, on ne peut pas connaître le logiciel proprietaire et on passe les premières 
semaines de travail à se former dessus, ce projet nous a donc appris à se former nous même. La durée 
du projet est assez longue pour cela même si cela passe très vite et qu'il y a forcements des choses
que l'on ne peut pas implementer, on a quand même le temps d'arriver à une version fonctionnelle ou 
vraiment pas loin. 

## Guillaume

## Globale

