# Introduction
## Context

  Actuellement en license 3 informatique à l'université de Rennes 1 nous passons beacoup de temps à recherche des salles (princpialement dans à l'ISTIC qui est notre lieu principal de travail) ,lors de notre temps libre et afin de travailler, avec l'aide de l'emploi du temps qui nous est fournis. Cependant, l'ergonomie de cet outil pour trouver des salles n'étant pas la principal priorité de l'application ne permet pas une recherche simple et efficace. En effet, il faut vérifier l'emploi du temps des salles afin de savoir si elles sont disponibles ou non.

## Problematique

  Nous nous sommes alors posé la question de savoir qi il n'éxiterait pas un moyen plus simple de chercher et vérifier qu'une salle est disponible ou non ?

## Solution

  Nous avons décidé de créer une application android qui permetrait aux étudiants de pouvoir chercher ou de voir à l'aide d'une carte qu'elles sont les disponibles à un moment précis en utlisant les données de l'outil d'emploi du temps.

# Description du projet

  Application android qui affiche les salles disponibles sur un plan du campus en se basant sur le planning de la fac. Cette application permettrait aux élèves de trouver plus rapidement quelles salles sont disponibles et dans quels bâtiments.

## Cahier des charges

- Réalisable dans les 6 semaines qui nous sont données
- Réaliser une application Android fonctionnant sur un grand nombre d'appareil
- Stockage des données à l'aide d'une base de données
- Réaliser une carte manipulable : zoom et déplacement et click sur des zones représentant les bâtimens
- Réaliser le bâtiment de l'ISTIC complétement si posible (avec la possibilité de pouvoir étendre l'application à l'ensemble de l'université et d'autres universités)
- Réaliser une barre de recherche permettant de recherher une salle précise (dans les bâtiments traités)
- Réaliser un menu permettant d'accèder à la page d'acceuil, recherche avancé et de paramètres
- Réaliser un système de recherche avancé avec le choix de pouvoir choisir une date et une heure précise
- Réaliser une page de paramètres permettant de changer la langue de l'application (au moins 2), le format de la date et l'affichage ou non des salles indsiponibles / disponibles

## Répartition des tâches

Séparation du groupe de 4 en 2 groupes de 2
- 2 sur la partie BackEnd, elle même séparé en 2
  - Carte : Berrouche Issameddine
  - Navigation / Menu : Buan Tony
- 2 sur la partie FrontEnd
  - Carte :  Traitement des données et de la base de données

## Outils utilisés (+raisons)

Utilisation de Discord (https://discord.com/) pour pouvoir discuter entre tous les membres du groupe et de notre professeur tuteur à l'aide du chat vocal et avoir une  note écrite des idées / problèmes rencontrés avec le chat. Nous n'avons pas choisi une autre plateforme car discord est une plateforme que tous les membres du groupe connaisaient et utlisaient régulièrement contrairement à d'autre plateforme.

Utilisation d'Android Studio (https://developer.android.com/studio) afin de réaliser le projet car il s'agit d'un des IDE les plus connus et utlisé pour réaliser des application androids. De plus, une documentation importante et de nombreuses librairies sur internet qui nous permet d'avoir plus d'outils à disposition pour répondre aux problèmes rencontrés ou encore de ne pas écrire du code trop complexe qui nous prendrait trop de temps pour un projet de 6 semaines.
Sachant que les deux seules langages de programmation disponibles sur Android Studio sont java et kotlyn, nous avons décidé d'utiliser java.En effet, il s'agit d'un language de programmation que nous avons étudié ces trois dernères années et donc dont nous disposons de bases solides contraiment à kotlyn.

Utilisation de Github (https://github.com/) qui a permis de partager le code entre toutes les personnes du groupes. Nous savions déjà utilisé la plateforme grâce aux cours que nous avions eu sur Gitlab (GEN). Nous avons cependant préféré utilisé GitHub afin de garder une trace de notre travail après nos études (ne sachant pas mais supposant que nos comtes Gitlab allaient âtre supprimés). De plus, une extension était disponible dans Android Studio ce qui nous a permis de fair le lien entre les deux très facilement. Enfin, un système de hiérarchie très pratique grâce aux commits qui nous a permis de retourner sur d'ancienne version en cas de problème avec le code actuelle.
- Berrouche Issameddine (RealHephaistos)
- Buan Tony (tbuan, NoctTB)
	- tbuan (commits réalisé depuis Android Studio) ->  N'est pas un compte GithHub donc les commits n'apparaissent pas dans les statistiques
- Hamono Morvan (MorvanH)
- Vachez Guillaume (xelphire)

Utilisation de SQLite (https://www.sqlite.org/index.html) afin de pouvoir stocker les données nécessaires pour le bon fonctionnement de l'application, c'est à dire les données de l'emploi du temps. Nous avons décidé d'utiliser SQLite car nous savions déjà utlisé mySQL (DSN) et afin de pouvoir garder notre travail pour le futur et car nous n'avions pas besoin d'une table de données composé d'un nombre de données très importants nous avons favorisé SQLite a SQL.

Utilisation d'Azure (https://azure.microsoft.com/fr-fr/) afin de stocker la base de donnée en ligne et de ne pas avoir besoin de la stocké sur l'application et donc réduire la taille de celle-ci. Nous avons utilisé Azure car cela est géré par microsoft et qu'un compte gratuit était disponible car nous somme étudiants. De plus des caractéeistiques largement suffisante pour notre base de donnée (débit,...).

## Mise en Oeuvre

- Carnet de Bord

# Gestion du projet

## Partie commune
## Base de donnée
## Base de donnée locale
## Barre de recherche
## Menu
## Paramettres
## Recherche avancée
## Vue de la carte du campus
  Afin de satisfaire le cahier des charges, l'utilisateur doit pouvoir zoomer et déplacer la carte du campus (idéalement avec un défilement fluide), ainsi que cliquer sur les batiments. La carte du campus étant trop grande et trop complexe pour etre stocké sous format SVG, nous n'avons pas pu utiliser la même méthode que pour la vue des bâtiments.
  
  Pour le déplacement de la carte, Android Studio n'offrant pas de solutions natives permettant de gérer le déplacement sur des images, nous avions initiallement pensé à programmer nous même un composant permettant de zoomer de de scroller dans deux dimmentions. Mais après une version beta techniquement fonctionnelle, bien que sans mouvement fluide, nous nous sommes rendus compte qu'il serait plus facile d'implémenter la librarie [PhotoView](https://github.com/Baseflow/PhotoView).
  
  Pour rendre certaines parties de la cartes intéractives, nous avons utilisé [un outils permettant de créer des imagemap](https://www.image-map.net/). Ce site permet de tracer des formes sur une image et génère automatiquement un fichier XML contenant les coordonés des points du polygone. La classe MapPhotoView (notre implémentation de PhotoView) utilise ce fichier pour créer une liste de polygones. Quand le composant MapPhotoView de l'activité principale détecte un click, il parcour la liste des polygones pour vérifier si l'utilisateur a cliquer sur un batiment, et ouvre la vue batiment correspondante si c'est le cas.
## Vue des batiments
## Validation de l'implémentation

# Conclusion

## Issam
## Tony

Un projet que j'ai fortement apprécié réalisé. Un projet libre sur un sujet qui me plaisait et dont les objectifs finaux ont été réalisé. N'étant pas spécialement adepte de la création de code j'ai beacoup aimé réalisé l'interface graphique de l'application grâce aux outils fournis par Android Studio. De nombreux petits détails qui posent souvent problèmes mais que j'ai trouvé satisfaisant à résoudre. Un projet totalement libre qui a cependant quelques contraintes. En effet je trouve que l'on peut perdre facilement la notion du temps de travail, ne pas savoir si on a travaillé assez, cependant ces doutes soont souvent remis en cause lors des réunions avec le professeur tuteur ce qui je trouve est une bonne chose. Enfin, comme dans tous projets ils est difficile de savoir par ou commencé car contrairement aux projets réalisés en cours nous ne sommes pas guidé.
## Morvan
## Guillaume
## Globale

