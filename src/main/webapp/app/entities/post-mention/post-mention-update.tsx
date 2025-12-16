import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, FormText, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getPostFeeds } from 'app/entities/post-feed/post-feed.reducer';
import { getEntities as getUserLites } from 'app/entities/user-lite/user-lite.reducer';
import { createEntity, getEntity, reset, updateEntity } from './post-mention.reducer';

export const PostMentionUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const postFeeds = useAppSelector(state => state.postFeed.entities);
  const userLites = useAppSelector(state => state.userLite.entities);
  const postMentionEntity = useAppSelector(state => state.postMention.entity);
  const loading = useAppSelector(state => state.postMention.loading);
  const updating = useAppSelector(state => state.postMention.updating);
  const updateSuccess = useAppSelector(state => state.postMention.updateSuccess);

  const handleClose = () => {
    navigate(`/post-mention${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getPostFeeds({}));
    dispatch(getUserLites({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    values.createdDate = convertDateTimeToServer(values.createdDate);
    values.lastModifiedDate = convertDateTimeToServer(values.lastModifiedDate);
    values.deletedDate = convertDateTimeToServer(values.deletedDate);

    const entity = {
      ...postMentionEntity,
      ...values,
      originPost: postFeeds.find(it => it.id.toString() === values.originPost?.toString()),
      mentionedUser: userLites.find(it => it.id.toString() === values.mentionedUser?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          createdDate: displayDefaultDateTime(),
          lastModifiedDate: displayDefaultDateTime(),
          deletedDate: displayDefaultDateTime(),
        }
      : {
          ...postMentionEntity,
          createdDate: convertDateTimeFromServer(postMentionEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(postMentionEntity.lastModifiedDate),
          deletedDate: convertDateTimeFromServer(postMentionEntity.deletedDate),
          originPost: postMentionEntity?.originPost?.id,
          mentionedUser: postMentionEntity?.mentionedUser?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="monsterdamChicksApp.postMention.home.createOrEditLabel" data-cy="PostMentionCreateUpdateHeading">
            <Translate contentKey="monsterdamChicksApp.postMention.home.createOrEditLabel">Create or edit a PostMention</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="post-mention-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('monsterdamChicksApp.postMention.createdDate')}
                id="post-mention-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.postMention.lastModifiedDate')}
                id="post-mention-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.postMention.createdBy')}
                id="post-mention-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.postMention.lastModifiedBy')}
                id="post-mention-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.postMention.deletedDate')}
                id="post-mention-deletedDate"
                name="deletedDate"
                data-cy="deletedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="post-mention-originPost"
                name="originPost"
                data-cy="originPost"
                label={translate('monsterdamChicksApp.postMention.originPost')}
                type="select"
              >
                <option value="" key="0" />
                {postFeeds
                  ? postFeeds.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="post-mention-mentionedUser"
                name="mentionedUser"
                data-cy="mentionedUser"
                label={translate('monsterdamChicksApp.postMention.mentionedUser')}
                type="select"
                required
              >
                <option value="" key="0" />
                {userLites
                  ? userLites.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/post-mention" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default PostMentionUpdate;
