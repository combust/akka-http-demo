package demo.core.api

/**
  * Created by hollinwilkins on 3/28/16.
  */
case class Artist(slug: Option[String] = None, name: String, genre: String)
case class CreateArtistRequest(artist: Artist)
case class CreateArtistResponse(artist: Artist)

case class ReadArtistRequest(slug: String)
case class ReadArtistResponse(artist: Artist)

case class Song(slug: Option[String] = None, artistSlug: Option[String] = None, name: String, duration: Int)
case class CreateSongRequest(song: Song)
case class CreateSongResponse(song: Song)

case class ReadSongsRequest(artistSlug: String)
case class ReadSongsResponse(songs: Seq[Song])
