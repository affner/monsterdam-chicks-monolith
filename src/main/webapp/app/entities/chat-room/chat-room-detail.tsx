import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './chat-room.reducer';

export const ChatRoomDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const chatRoomEntity = useAppSelector(state => state.chatRoom.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="chatRoomDetailsHeading">
          <Translate contentKey="monsterdamChicksApp.chatRoom.detail.title">ChatRoom</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{chatRoomEntity.id}</dd>
          <dt>
            <span id="lastAction">
              <Translate contentKey="monsterdamChicksApp.chatRoom.lastAction">Last Action</Translate>
            </span>
          </dt>
          <dd>{chatRoomEntity.lastAction}</dd>
          <dt>
            <span id="lastConnectionDate">
              <Translate contentKey="monsterdamChicksApp.chatRoom.lastConnectionDate">Last Connection Date</Translate>
            </span>
          </dt>
          <dd>
            {chatRoomEntity.lastConnectionDate ? (
              <TextFormat value={chatRoomEntity.lastConnectionDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="muted">
              <Translate contentKey="monsterdamChicksApp.chatRoom.muted">Muted</Translate>
            </span>
          </dt>
          <dd>{chatRoomEntity.muted ? 'true' : 'false'}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="monsterdamChicksApp.chatRoom.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {chatRoomEntity.createdDate ? <TextFormat value={chatRoomEntity.createdDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="monsterdamChicksApp.chatRoom.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {chatRoomEntity.lastModifiedDate ? (
              <TextFormat value={chatRoomEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="monsterdamChicksApp.chatRoom.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{chatRoomEntity.createdBy}</dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="monsterdamChicksApp.chatRoom.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{chatRoomEntity.lastModifiedBy}</dd>
          <dt>
            <span id="isDeleted">
              <Translate contentKey="monsterdamChicksApp.chatRoom.isDeleted">Is Deleted</Translate>
            </span>
          </dt>
          <dd>{chatRoomEntity.isDeleted ? 'true' : 'false'}</dd>
          <dt>
            <span id="participantId">
              <Translate contentKey="monsterdamChicksApp.chatRoom.participantId">Participant Id</Translate>
            </span>
          </dt>
          <dd>{chatRoomEntity.participantId}</dd>
        </dl>
        <Button tag={Link} to="/chat-room" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/chat-room/${chatRoomEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ChatRoomDetail;
