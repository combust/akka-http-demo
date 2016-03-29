package demo.core.api

import java.util.UUID

/**
  * Created by hollinwilkins on 3/28/16.
  */
case class Artist(uid: Option[UUID] = None, name: String, popularity: Int)
case class CreateArtistRequest(artist: Artist)
case class CreateArtistResponse(artist: Artist)

case class ReadArtistRequest(uid: UUID)
case class ReadArtistResponse(artist: Artist)

case class Song(uid: Option[UUID] = None, artistUid: Option[UUID] = None, name: String, duration: Int)
case class CreateSongRequest(song: Song)
case class CreateSongResponse(song: Song)

case class ReadSongsRequest(artistUid: UUID)
case class ReadSongsResponse(songs: Seq[Song])
