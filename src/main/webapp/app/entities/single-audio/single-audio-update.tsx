import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, FormText, Row } from 'reactstrap';
import { Translate, ValidatedBlobField, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getContentPackages } from 'app/entities/content-package/content-package.reducer';
import { getEntities as getUserLites } from 'app/entities/user-lite/user-lite.reducer';
import { createEntity, getEntity, reset, updateEntity } from './single-audio.reducer';

export const SingleAudioUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const contentPackages = useAppSelector(state => state.contentPackage.entities);
  const userLites = useAppSelector(state => state.userLite.entities);
  const singleAudioEntity = useAppSelector(state => state.singleAudio.entity);
  const loading = useAppSelector(state => state.singleAudio.loading);
  const updating = useAppSelector(state => state.singleAudio.updating);
  const updateSuccess = useAppSelector(state => state.singleAudio.updateSuccess);

  const handleClose = () => {
    navigate(`/single-audio${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getContentPackages({}));
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
      ...singleAudioEntity,
      ...values,
      contentPackage: contentPackages.find(it => it.id.toString() === values.contentPackage?.toString()),
      creator: userLites.find(it => it.id.toString() === values.creator?.toString()),
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
          ...singleAudioEntity,
          createdDate: convertDateTimeFromServer(singleAudioEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(singleAudioEntity.lastModifiedDate),
          deletedDate: convertDateTimeFromServer(singleAudioEntity.deletedDate),
          contentPackage: singleAudioEntity?.contentPackage?.id,
          creator: singleAudioEntity?.creator?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="monsterdamChicksApp.singleAudio.home.createOrEditLabel" data-cy="SingleAudioCreateUpdateHeading">
            <Translate contentKey="monsterdamChicksApp.singleAudio.home.createOrEditLabel">Create or edit a SingleAudio</Translate>
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
                  id="single-audio-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedBlobField
                label={translate('monsterdamChicksApp.singleAudio.thumbnail')}
                id="single-audio-thumbnail"
                name="thumbnail"
                data-cy="thumbnail"
                isImage
                accept="image/*"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.singleAudio.thumbnailS3Key')}
                id="single-audio-thumbnailS3Key"
                name="thumbnailS3Key"
                data-cy="thumbnailS3Key"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedBlobField
                label={translate('monsterdamChicksApp.singleAudio.content')}
                id="single-audio-content"
                name="content"
                data-cy="content"
                openActionLabel={translate('entity.action.open')}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.singleAudio.contentS3Key')}
                id="single-audio-contentS3Key"
                name="contentS3Key"
                data-cy="contentS3Key"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.singleAudio.duration')}
                id="single-audio-duration"
                name="duration"
                data-cy="duration"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.singleAudio.createdDate')}
                id="single-audio-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.singleAudio.lastModifiedDate')}
                id="single-audio-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.singleAudio.createdBy')}
                id="single-audio-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.singleAudio.lastModifiedBy')}
                id="single-audio-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.singleAudio.deletedDate')}
                id="single-audio-deletedDate"
                name="deletedDate"
                data-cy="deletedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="single-audio-contentPackage"
                name="contentPackage"
                data-cy="contentPackage"
                label={translate('monsterdamChicksApp.singleAudio.contentPackage')}
                type="select"
              >
                <option value="" key="0" />
                {contentPackages
                  ? contentPackages.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="single-audio-creator"
                name="creator"
                data-cy="creator"
                label={translate('monsterdamChicksApp.singleAudio.creator')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/single-audio" replace color="info">
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

export default SingleAudioUpdate;
